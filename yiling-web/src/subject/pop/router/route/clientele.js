const route = [
  {
    path: '/clientele',
    alwaysShow: true,
    redirect: '/clientele/channel',
    name: '',
    meta: {
      title: '客户管理',
      icon: 'icon3',
      identify: 'pop:clientele',
      roles: []
    },
    children: [
      {
        path: 'channel',
        name: 'Channel',
        meta: {
          title: '渠道商管理',
          icon: '',
          identify: 'pop:clientele:channel'
        },
        component: () => import('@/subject/pop/views/clientele/channel/index')
      },
      {
        path: 'channel_edit/:customerEid?/:eid?',
        name: 'ChannelEdit',
        meta: {
          title: '渠道商详情',
          icon: '',
          identify: 'pop:clientele:channel:edit',
          activeMenu: ['/clientele/channel'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views/clientele/channel/detail/index'),
        hidden: true
      },
      {
        path: 'list',
        name: 'List',
        meta: { title: '客户列表', icon: '', identify: 'pop:clientele:list' },
        component: () => import('@/subject/pop/views/clientele/list/index')
      },
      {
        path: 'list_edit/:customerEid?/:eid?',
        name: 'ListEdit',
        meta: {
          title: '编辑资料',
          icon: '',
          identify: 'pop:clientele:list:edit',
          activeMenu: ['/clientele/list'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views/clientele/list/detail/index'),
        hidden: true
      },
      {
        path: 'group',
        name: 'Group',
        meta: { title: '客户分组', icon: '', identify: 'pop:clientele:group' },
        component: () => import('@/subject/pop/views/clientele/group/index')
      },
      {
        path: 'group_edit/:customerGroupId/:name',
        name: 'GroupEdit',
        meta: {
          title: '编辑分组',
          icon: '',
          identify: 'pop:clientele:group:edit',
          activeMenu: ['/clientele/group'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views/clientele/group/detail/index'),
        hidden: true
      }
    ]
  }
]

export default route
