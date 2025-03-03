#!/usr/bin/env python
# coding: utf-8

# In[ ]:


from faker import Faker
import json
import requests
import random
import time
from concurrent.futures import ThreadPoolExecutor, as_completed
from tqdm import tqdm
fake = Faker('zh_CN')


# In[ ]:


api_url = "http://localhost:7000"
def path_join(*args, base_url = api_url):
    return base_url + "/".join(args)
def to_json(data, format = False):
    args = {
        "ensure_ascii": False
    }
    if format: args["indent"] = 4
    return json.dumps(data, **args)


# In[ ]:


def create_session(username = 'caixypromise', password = 'as123456789'):
    url = path_join("/api/user/login")
    data = {
        "userAccount": username,
        "userPassword": password
    }
    session = requests.session()
    succeed = session.post(url, json=data).json()
    if succeed.get('code') is None or succeed.get('code') != 0:
        print(f"登录失败: {succeed}")
    print(f"登录成功: {to_json(succeed, True)}")
    return session
session = create_session()


# In[ ]:


def get(url, params=None):
    response = session.get(path_join(url), params=params, timeout=5)
    return response.json()


# In[ ]:


def post(url, data=None):
    response = session.post(path_join(url), json=data, timeout=5)
    return response.json()


# In[ ]:


def put(url, data=None):
    response = session.put(path_join(url), json=data, timeout=5)
    return response.json()


# In[ ]:


def delete(url, data=None):
    response = session.delete(path_join(url), json=data, timeout=5)
    return response.json()


# In[ ]:


subject_names = [
    "数学", "语文", "英语", "物理", "化学", "生物", "历史", "地理", "政治", "计算机",
    "美术", "音乐", "体育", "心理学", "经济学", "法学", "哲学", "社会学", "统计学", "医学",
    "环境科学", "天文学", "人工智能", "大数据", "数据结构", "操作系统", "网络安全", "软件工程",
    "微积分", "线性代数", "概率论", "数理逻辑", "编译原理", "生物化学", "电子工程", "自动控制",
    "机器人学", "农业科学", "食品安全", "旅游管理", "市场营销", "国际贸易", "广告学", "新闻学",
    "传播学", "艺术设计", "摄影", "电影制作", "表演", "舞蹈", "体育健康", "运动科学",
    "营养学", "护理学", "药学", "生物工程", "遗传学", "分子生物学", "生态学", "气象学",
    "地质学", "测绘学", "水利工程", "土木工程", "交通工程", "航天工程", "新能源科学",
    "光学", "核物理", "无线通信", "高等数学", "初等数学", "代数", "几何", "拓扑学",
    "逻辑学", "伦理学", "心理咨询", "企业管理", "财务管理", "会计学", "税务学",
    "人力资源管理", "供应链管理", "物联网", "虚拟现实", "增强现实", "区块链", "智能制造",
    "精密仪器", "化工", "生物医学工程", "网络编程", "前端开发", "后端开发", "云计算",
    "数据库管理", "移动开发", "自动化控制", "金融学", "保险学"
]
# 添加科目
for name in subject_names:
    grade_max = 100
    grade_min = 0
    grade_excellent = random.randint(85, grade_max)
    grade_fail = random.randint(grade_min, 60)
    credit_hours = random.randint(30, 100)
    course_type = 1 if random.random() < 0.5 else 0
    grade_credit = float(random.randint(1, 12))
    payload = {
        "name": name,
        "gradeMax": grade_max,
        "gradeMin": grade_min,
        "gradeExcellent": grade_excellent,
        "gradeFail": grade_fail,
        "creditHours": credit_hours,
        "courseType": course_type,
        "gradeCredit": grade_credit
    }
    response = post('/api/subject/add', data=payload)
    print(response)


# In[ ]:


def get_department_data():
    """请求学院、专业、班级数据"""
    try:
        response = post('/api/classes/get/classes', data={})
        return response
    except Exception as e:
        print("请求学院、专业、班级数据失败：", e)
        return None

def add_student(stuName, stuSex, stuDeptId, stuMajorId, stuClassId, stuId):
    """发送添加学生的请求"""
    payload = {
        "stuName": stuName,
        "stuSex": stuSex,
        "stuDeptId": stuDeptId,
        "stuMajorId": stuMajorId,
        "stuClassId": stuClassId,
        "stuId": stuId
    }
    try:
        response = post('/api/student/add', data=payload)
        if response.get('code') == 0:
            return None
        else:
            return f"学生 {stuName} 添加失败，状态码：{response.get('code')}，响应：{response.get('message')}"
    except Exception as e:
        return f"学生 {stuName} 添加失败，异常：{str(e)}"
    
def mock_student_info():
    # 获取学院、专业、班级数据
    dept_data = get_department_data()
    if not dept_data or dept_data.get("code") != 0:
        print("获取学院、专业、班级数据失败，退出程序")
        return

    # 提取每个班级的相关信息：学院id、专业id、班级id
    class_info_list = []
    for college in dept_data["data"]:
        dept_id = int(college["value"])
        for major in college.get("children", []):
            major_id = int(major["value"])
            for clazz in major.get("children", []):
                class_id = int(clazz["value"])
                class_info_list.append((dept_id, major_id, class_id, clazz["label"]))

    total_students = len(class_info_list) * 50
    print(f"将为每个班级生成 50 个学生，总共 {total_students} 个学生记录")

    failures = []  # 用于记录请求失败的信息
    futures = []
    max_workers = 10

    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        # 为每个班级生成 50 个学生的添加请求任务
        for dept_id, major_id, class_id, class_label in class_info_list:
            for _ in range(50):
                stuName = fake.name()             # 生成中文姓名
                stuSex = random.choice([1, 2])      # 随机性别（1 或 2）
                stuId = "S" + str(random.randint(10000000, 99999999))  # 生成学号
                futures.append(
                    executor.submit(add_student, stuName, stuSex, dept_id, major_id, class_id, stuId)
                )

        # 使用 tqdm 监控任务进度
        for future in tqdm(as_completed(futures), total=len(futures), desc="添加学生进度"):
            result = future.result()
            # 失败的请求记录下来
            if result:
                failures.append(result)
                print(result)

    print("学生添加完成！")
    if failures:
        print("以下请求失败：")
        for failure in failures:
            print(failure)
mock_student_info()


# In[ ]:


def add_teacher(teacherId, teacherName, teacherSex, teacherDeptId, teacherMajorId):
    """发送添加教师的请求"""
    payload = {
        "teacherId": teacherId,
        "teacherName": teacherName,
        "teacherSex": teacherSex,
        "teacherDeptId": teacherDeptId,
        "teacherMajorId": teacherMajorId
    }
    try:
        response = post('/api/teacher/add', data=payload)
        if response.get("code") == 0:
            return None  # 成功则返回 None
        else:
            return f"教师 {teacherName}（工号: {teacherId}） 添加失败，状态码：{response.get('code')}，错误信息：{response.get('message')}"
    except Exception as e:
        return f"教师 {teacherName}（工号: {teacherId}） 添加失败，异常：{str(e)}"
    
def mock_teacher_info():
    # 获取学院、专业、班级数据
    dept_data = get_department_data()
    if not dept_data or dept_data.get("code") != 0:
        print("获取学院、专业、班级数据失败，退出程序")
        return

    # 提取每个专业的相关信息：学院id、专业id
    major_info_list = []
    for college in dept_data["data"]:
        dept_id = int(college["value"])
        for major in college.get("children", []):
            major_id = int(major["value"])
            major_info_list.append((dept_id, major_id, major["label"]))
    total_teachers = len(major_info_list) * 20
    print(f"将为每个专业生成 20 名教师，总共 {total_teachers} 名教师记录")

    failures = []  # 用于记录请求失败的信息
    futures = []
    max_workers = 10

    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        # 为每个专业生成 20 名教师的添加请求任务
        for dept_id, major_id, major_label in major_info_list:
            for _ in range(20):
                teacherName = fake.name()             # 生成中文姓名
                teacherSex = random.choice([1, 2])    # 随机性别（1 男，2 女）
                teacherId = "T" + str(random.randint(10000000, 99999999))  # 生成教师工号
                futures.append(
                    executor.submit(add_teacher, teacherId, teacherName, teacherSex, dept_id, major_id)
                )

        # 使用 tqdm 监控任务进度
        for future in tqdm(as_completed(futures), total=len(futures), desc="添加教师进度"):
            result = future.result()
            # 失败的请求记录下来
            if result:
                failures.append(result)

    print("教师添加完成！")
    if failures:
        print("以下请求失败：")
        for failure in failures:
            print(failure)
mock_teacher_info()


# In[ ]:




