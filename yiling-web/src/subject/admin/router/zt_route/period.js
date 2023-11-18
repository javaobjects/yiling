const route = [
  {
    path: '/period',
    redirect: '/period/buyer_list',
    alwaysShow: true,
    name: '',
    meta: { title: '账期管理', icon: 'icon10', identify: 'zt:period' },
    children: [
      {
        path: 'buyer_list',
        name: 'BuyerList',
        meta: { title: '采购商账期列表', icon: '', identify: 'zt:period:buyer_list'},
        component: () => import('@/subject/admin/views_zt/period/buyer_list')
      }
    ]
  }
]

export default route
