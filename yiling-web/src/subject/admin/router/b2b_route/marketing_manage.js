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
        component: () => import('@/subject/admin/views_b2b/marketing_manage/marketing_list')
      },
      {
        path: 'marketing_edit/:type?/:operationType?/:id?',
        name: 'MarketingEdit',
        meta: {
          title: '创建促销活动',
          icon: '',
          identify: 'b2b:marketing_manage:marketing_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/marketing_edit'),
        hidden: true
      },
      {
        path: 'special_activity_list',
        name: 'SpecialActivityList',
        meta: {
          title: '专场活动',
          icon: '',
          identify: 'b2b:marketing_manage:special_activity_list'
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/special_activity_list')
      },
      {
        path: 'special_activity_edit/:operationType?/:id?',
        name: 'SpecialActivityEdit',
        meta: {
          title: '创建专场活动',
          icon: '',
          identify: 'b2b:marketing_manage:special_activity_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/special_activity_edit'),
        hidden: true
      },
      {
        path: 'subscribe_list',
        name: 'SubscribeList',
        meta: {
          title: '预约管理',
          icon: '',
          identify: 'b2b:marketing_manage:subscribe_list'
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/subscribe_list')
      },
      {
        path: 'tactics_activity_list',
        name: 'TacticsActivityList',
        meta: {
          title: '策略满赠',
          icon: '',
          identify: 'b2b:marketing_manage:tactics_activity_list'
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/tactics_activity_list')
      },
      {
        path: 'tactics_activity_edit/:operationType?/:id?',
        name: 'TacticsActivityEdit',
        meta: {
          title: '添加策略满赠',
          icon: '',
          identify: 'b2b:marketing_manage:tactics_activity_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/tactics_activity_edit'),
        hidden: true
      },
      {
        path: 'pay_promotion_list',
        name: 'PayPromotionList',
        meta: {
          title: '支付促销',
          icon: '',
          identify: 'b2b:marketing_manage:pay_promotion_list'
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/pay_promotion_list')
      },
      {
        path: 'pay_promotion_edit/:operationType?/:id?',
        name: 'PayPromotionEdit',
        meta: {
          title: '添加支付促销',
          icon: '',
          identify: 'b2b:marketing_manage:pay_promotion_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/marketing_manage/pay_promotion_edit'),
        hidden: true
      }
    ]
  }
]

export default route
