const route = [
  {
    path: '/b2b_clientele',
    alwaysShow: true,
    redirect: '/b2b_clientele/list',
    name: '',
    meta: {
      title: '客户管理',
      icon: 'icon3',
      identify: 'b2b:b2b_clientele',
      roles: []
    },
    children: [
      {
        path: 'b2b_list',
        name: 'B2BList',
        meta: { title: '客户列表', icon: '', identify: 'b2b:b2b_clientele:list' },
        component: () => import('@/subject/pop/views_b2b/b2b_clientele/list/index')
      },
      {
        path: 'b2b_list_edit/:customerEid?/:eid?',
        name: 'B2bListEdit',
        meta: {
          title: '编辑资料',
          icon: '',
          identify: 'b2b:b2b_clientele:list_edit',
          activeMenu: ['/b2b_clientele/b2b_list'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/b2b_clientele/list/detail/index'),
        hidden: true
      },
      {
        path: 'b2b_group',
        name: 'B2bGroup',
        meta: { title: '客户分组', icon: '', identify: 'b2b:b2b_clientele:group' },
        component: () => import('@/subject/pop/views_b2b/b2b_clientele/group/index')
      },
      {
        path: 'b2b_group_edit/:customerGroupId/:name',
        name: 'B2bGroupEdit',
        meta: {
          title: '编辑分组',
          icon: '',
          identify: 'b2b:b2b_clientele:group_edit',
          activeMenu: ['/b2b_clientele/b2b_group'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/b2b_clientele/group/detail/index'),
        hidden: true
      },
      {
        path: 'b2b_apply_list',
        name: 'B2BApplyList',
        meta: { title: '采销审核', icon: '', identify: 'b2b:b2b_clientele:b2b_apply_list' },
        component: () => import('@/subject/pop/views_b2b/b2b_clientele/apply_list/index')
      },
      {
        path: 'b2b_apply_edit/:customerEid',
        name: 'B2bApplyEdit',
        meta: {
          title: '审核',
          icon: '',
          identify: 'b2b:b2b_clientele:b2b_apply_edit',
          activeMenu: ['/b2b_clientele/b2b_apply_list'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/b2b_clientele/apply_list/detail/index'),
        hidden: true
      }
    ]
  }
]

export default route
