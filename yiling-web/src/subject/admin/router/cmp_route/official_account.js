//公众号管理
const route = [
  {
    path: '/official_account',
    redirect: '/official_account/menu',
    name: 'OfficialAccount',
    meta: { title: '公众号管理', icon: 'official_account', identify: 'cmp:official_account' },
    children: [
      {
        path: 'menu',
        name: 'Menu',
        meta: {
          title: '公众号菜单管理',
          icon: '',
          identify: 'cmp:official_account:menu'
        },
        component: () => import('@/subject/admin/views_cmp/official_account/menu')
      },
      {
        path: 'welcome_message',
        name: 'WelcomeMessage',
        meta: {
          title: '欢迎语管理',
          icon: '',
          identify: 'cmp:official_account:welcome_message'
        },
        component: () => import('@/subject/admin/views_cmp/official_account/welcome_message')
      },
      {
        path: 'welcome_message_establish/:id',
        name: 'WelcomeMessageEstablish',
        meta: {
          title: '编辑/创建',
          icon: '',
          identify: 'cmp:official_account:welcome_message_establish',
          activeMenu: ['/official_account/welcome_message'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/official_account/welcome_message/establish')
      }
    ]
  }
]
export default route