const route = [
  {
    path: '/system',
    redirect: '/system/user_manage',
    name: '',
    meta: { title: '系统管理', icon: 'icon6', identify: 'sys:system' },
    children: [
      {
        path: 'user_manage',
        name: 'UserManage',
        meta: { title: '用户管理', icon: '', identify: 'sys:system:user_manage' },
        component: () => import('@/subject/admin/views/system/user_manage/index')
      },
      {
        path: 'role_manage',
        name: 'RoleManage',
        meta: { title: '角色管理', icon: '', identify: 'sys:system:role_manage' },
        component: () => import('@/subject/admin/views/system/role_manage/index')
      },
      {
        path: 'menu_manage',
        name: 'MenuManage',
        meta: { title: '菜单管理', icon: '', identify: 'sys:system:menu_manage' },
        component: () => import('@/subject/admin/views/system/menu_manage/index')
      },
      {
        path: 'dictionaries_manage',
        name: 'DicManage',
        meta: { title: '字典管理', icon: '', identify: 'sys:system:dictionaries_manage' },
        component: () => import('@/subject/admin/views/system/dictionaries_manage/index')
      },
      {
        path: 'log_manage',
        name: 'LogManage',
        meta: { title: '操作日志', icon: '', identify: 'sys:system:log_manage' },
        component: () => import('@/subject/admin/views/system/log_manage/index')
      },
      {
        path: 'login_log',
        name: 'LoginLog',
        meta: { title: '登录日志', icon: '', identify: 'sys:system:login_log' },
        component: () => import('@/subject/admin/views/system/login_log/index')
      },
      {
        path: 'sms_log',
        name: 'SmsLog',
        meta: { title: '短信日志', icon: '', identify: 'sys:system:sms_log' },
        component: () => import('@/subject/admin/views/system/sms_log/index')
      },
      {
        path: 'thesaurus_manage',
        name: 'ThesaurusManage',
        meta: { title: '词库管理', icon: '', identify: 'sys:system:thesaurus_manage' },
        component: () => import('@/subject/admin/views/system/thesaurus_manage/index')
      },
      {
        path: 'thesaurus_establis/:type?/:word?',
        name: 'ThesaurusEstablis',
        meta: {
          title: '新增/查看词库',
          icon: '',
          identify: 'sys:system:thesaurus_establis',
          activeMenu: ['/system/thesaurus_manage'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views/system/thesaurus_manage/establis/index')
      }
    ]
  }
]

export default route
