const route = [
  {
    path: '/zt_roles',
    alwaysShow: true,
    redirect: '/zt_roles/roles_index',
    name: '',
    meta: { title: '权限管理', icon: 'icon6', identify: 'zt:roles' },
    children: [
      {
        path: 'roles_index',
        name: 'ZtRolesIndex',
        meta: { title: '角色管理', icon: '', identify: 'zt:roles:role' },
        component: () => import('@/subject/pop/views_zt/roles/role')
      },
      {
        path: 'roles_department',
        name: 'ZtRolesDepartment',
        meta: { title: '部门管理', icon: '', identify: 'zt:roles:department' },
        component: () => import('@/subject/pop/views_zt/roles/department')
      },
      {
        path: 'roles_department_detail/:id',
        name: 'ZtRolesDepartmentDetail',
        meta: { title: '部门详情', icon: '', identify: 'zt:roles:department_detail', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/pop/views_zt/roles/department_detail'),
        hidden: true
      },
      {
        path: 'roles_job',
        name: 'ZtRolesJob',
        meta: { title: '职位管理', icon: '', identify: 'zt:roles:job' },
        component: () => import('@/subject/pop/views_zt/roles/job')
      },
      {
        path: 'roles_detail/:id?',
        name: 'ZtRolesDetail',
        meta: { title: '角色详情', icon: '', identify: 'zt:roles:detail', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/pop/views_zt/roles/role_detail'),
        hidden: true
      },
      {
        path: 'roles_user',
        name: 'ZtRolesUser',
        meta: { title: '员工管理', icon: '', identify: 'zt:roles:user' },
        component: () => import('@/subject/pop/views_zt/roles/user')
      }
    ]
  }
]

export default route
