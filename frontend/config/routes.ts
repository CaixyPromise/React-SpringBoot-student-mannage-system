export default [
  {path: '/user', layout: false, routes: [{path: '/user/login', component: './User/Login'}]},
  {
    icon: 'user',
    path: '/user-center',
    component: './Admin/User',
    name: "用户管理",
    access: 'canAdmin',
  },
  {
    icon: 'BankOutlined',
    path: '/department-data',
    name: '学院与专业管理',
    component: './Admin/DepartmentAndMajorList',
    access: 'canAdmin',
  },
  {
    icon: 'AuditOutlined',
    path: '/course-selection',
    name: '选课任务管理',
    access: 'canAdmin',
    component: './Admin/CourseSelection'
  },
  {
    icon: "BuildOutlined",
    path: "/semester",
    name: "学期管理",
    access: 'canAdmin',
    component: "./Admin/Semester"
  },
  {
    icon: 'DeploymentUnitOutlined',
    path: '/subject',
    access: 'canAdmin',
    name: '科目管理',
    component: './Admin/Subject'
  },
  {
    icon: 'TeamOutlined',
    path: '/student',
    name: '学生管理',
    access: 'canAdmin',
    component: './Admin/Student'
  },
  {
    icon: 'TeamOutlined',
    path: '/teacher',
    name: '教师管理',
    access: 'canAdmin',
    component: './Admin/Teacher'
  },
  {
    icon: 'AppstoreAddOutlined',
    path: "/select-course-task",
    name: "选课任务",
    access: "isStudent",
    component: "./Student/select-course"
  },
  {
    icon: 'AppstoreAddOutlined',
    path: "/my-grades",
    name: "我的成绩信息",
    access: "isStudent",
    component: "./Student/my-grades"
  },
  {
    icon: 'AppstoreAddOutlined',
    path: '/teacher/assigned/selection',
    name: '已分配选课任务',
    access: 'isTeacher',
    component: './Teacher/AssignedSelection'
  },
  {
    icon: 'FieldTimeOutlined',
    path: '/admin/registration-score',
    name: '登记成绩任务管理',
    access: 'canAdmin',
    component: './Admin/RegistrationScore'
  },
  {path: '/', redirect: '/analysis'},
  {path: '*', layout: false, component: './404'},
];
