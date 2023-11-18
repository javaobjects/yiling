const route = [
  {
    path: '/lottery',
    redirect: '/lottery/b2b_lottery_list',
    name: '',
    meta: { title: '抽奖活动', icon: 'lottery', identify: 'marketing:lottery' },
    children: [
      {
        path: 'b2b_lottery_list',
        name: 'B2bLotteryList',
        meta: {
          title: 'B2B抽奖',
          icon: '',
          identify: 'marketing:lottery:b2b_lottery_list'
        },
        component: () => import('@/subject/admin/views_marketing/lottery_activity/b2b_lottery_list')
      },
      {
        path: 'c_lottery_list',
        name: 'B2bLotteryList',
        meta: {
          title: '2C抽奖',
          icon: '',
          identify: 'marketing:lottery:c_lottery_list'
        },
        component: () => import('@/subject/admin/views_marketing/lottery_activity/b2b_lottery_list')
      },
      {
        path: 'lottery_edit/:operationType/:platformType/:id?',
        name: 'LotteryEdit',
        meta: {
          title: '添加抽奖活动',
          icon: '',
          identify: 'marketing:lottery:lottery_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_marketing/lottery_activity/lottery_edit'),
        hidden: true
      }
    ]
  }
]

export default route
