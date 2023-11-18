const route = [
  {
    path: '/b2b_period',
    redirect: '/b2b_period/index',
    alwaysShow: true,
    name: '',
    meta: { title: '账期管理b2b', icon: 'icon10', identify: 'b2b:period' },
    children: [
      {
        path: 'index',
        name: 'B2bPeriodIndex',
        meta: { title: '账期额度管理', icon: '', identify: 'b2b:period:list' },
        component: () => import('@/subject/pop/views_b2b/period/list')
      },
      {
        path: 'warn',
        name: 'B2bWarn',
        meta: { title: '账期到期提醒', icon: '', identify: 'b2b:period:warn' },
        component: () => import('@/subject/pop/views_b2b/period/warn_list')
      }
    ]
  }
]

export default route
