const route = [
  {
    path: '/zt_clientele',
    alwaysShow: true,
    redirect: '/zt_clientele/list',
    name: '',
    meta: {
      title: '客户管理',
      icon: 'icon3',
      identify: 'zt:zt_clientele',
      roles: []
    },
    children: [
      {
        path: 'list',
        name: 'ZtList',
        meta: { title: '客户列表', icon: '', identify: 'zt:zt_clientele:list' },
        component: () => import('@/subject/pop/views_zt/zt_clientele/list/index')
      },
      {
        path: 'list_edit/:customerEid?/:eid?',
        name: 'ZtListEdit',
        meta: {
          title: '编辑资料',
          icon: '',
          identify: 'zt:zt_clientele:edit',
          activeMenu: ['/zt_clientele/list'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_zt/zt_clientele/list/detail/index'),
        hidden: true
      }
    ]
  }
]

export default route
