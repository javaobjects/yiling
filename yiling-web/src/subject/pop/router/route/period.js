const route = [
  {
    path: '/period',
    redirect: '/period/index',
    alwaysShow: true,
    name: '',
    meta: { title: '账期管理', icon: 'icon10', identify: 'pop:period' },
    children: [
      {
        path: 'index',
        name: 'PeriodIndex',
        meta: { title: '账期额度管理', icon: '', identify: 'pop:period:list', type: 1 },
        component: () => import('@/subject/pop/views/period/list')
      },
      {
        path: 'warn',
        name: 'Warn',
        meta: { title: '账期到期提醒', icon: '', identify: 'pop:period:warn' },
        component: () => import('@/subject/pop/views/period/warn_list')
      },
      {
        path: 'apply',
        name: 'Apply',
        meta: { title: '临时额度审核', icon: '', identify: 'pop:period:apply' },
        component: () => import('@/subject/pop/views/period/apply_list')
      },
      {
        path: 'buyer_list',
        name: 'BuyerList',
        meta: {
          title: '采购商账期列表',
          icon: '',
          identify: 'pop:period:buyer_list'
        },
        component: () => import('@/subject/pop/views/period/buyer_list')
      }
    ]
  }
]

export default route
