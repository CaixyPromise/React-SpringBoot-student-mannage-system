export default [
  {path: '/user', layout: false, routes: [{path: '/user/login', component: './User/Login'}]},
  {icon: 'PieChartOutlined', path: '/analysis', name: '成绩分析', component: './Admin/Analysis'},
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
    component: "./select-course"
  },
  {path: '/', redirect: '/analysis'},
  {path: '*', layout: false, component: './404'},
];
