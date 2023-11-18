const route = [
  {
    path: '/marketing_manage',
    alwaysShow: true,
    redirect: '/marketing_manage/marketing_list',
    name: '',
    meta: { title: '营销管理', icon: 'icon15', identify: 'b2b:marketing_manage' },
    children: [
      {
        path: 'marketing_list',
        name: 'MarketingList',
        meta: {
          title: '营销活动',
          icon: '',
          identify: 'b2b:marketing_manage:marketing_list'
        },
        component: () => import('@/subject/pop/views_b2b/marketing_manage/marketing_list')
      },
      {
        path: 'marketing_edit/:operationType?/:id?',
        name: 'MarketingEdit',
        meta: {
          title: '创建促销活动',
          icon: '',
          identify: 'b2b:discount_coupon:marketing_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/marketing_manage/marketing_edit'),
        hidden: true
      }
    ]
  }
]

export default route
