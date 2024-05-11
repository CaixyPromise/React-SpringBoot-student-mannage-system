export default [
    { path: '/user', layout: false, routes: [ { path: '/user/login', component: './User/Login' } ] },
    { icon: 'PieChartOutlined', path: '/analysis', name: '成绩分析', component: './Admin/Analysis' },

    { icon: 'user', path: '/user-center', component: './Admin/User', name: "用户管理" },
    {
        icon: 'BankOutlined', path: '/department-data', name: '学院与专业管理',
        component: './Admin/DepartmentAndMajorList'
    },
    { icon: 'DeploymentUnitOutlined', path: '/subject', name: '科目管理', component: './Admin/Subject' },
    { icon: 'TeamOutlined', path: '/student', name: '学生管理', component: './Admin/Student' },
    { path: '/', redirect: '/analysis' },
    { path: '*', layout: false, component: './404' },
];
