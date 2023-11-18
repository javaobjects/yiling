const route = [
  {
    path: '/gift_manage',
    alwaysShow: true,
    redirect: '/gift_manage/gift_list',
    name: '',
    meta: { title: '赠品管理', icon: 'icon19', identify: 'marketing:gift_manage' },
    children: [
      {
        path: 'gift_list',
        name: 'GiftList',
        meta: {
          title: '赠品库',
          icon: '',
          identify: 'marketing:gift_manage:gift_list'
        },
        component: () => import('@/subject/admin/views_marketing/gift_manage/gift_list')
      },
      {
        path: 'gift_edit/:id?',
        name: 'GiftEdit',
        meta: {
          title: '商品基本信息',
          icon: '',
          identify: 'marketing:gift_manage:gift_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_marketing/gift_manage/gift_edit'),
        hidden: true
      }
    ]
  }
]

export default route
