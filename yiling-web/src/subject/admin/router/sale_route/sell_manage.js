const route = [
  {
    path: '/sell_manage',
    redirect: '/sell_manage/user_control',
    name: '',
    meta: { title: '锁定用户', icon: 'user_control', identify: 'sale:sell_manage' },
    children: [
      {
        path: 'user_control',
        name: 'UserControl',
        meta: { title: '锁定用户', icon: '', identify: 'sale:sell_manage:user_control' },
        component: () => import('@/subject/admin/views_sale/sell_manage/user_control/index')
      }
    ]
  }
]

export default route
