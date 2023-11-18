const route = [
  {
    path: '/buyOrder',
    redirect: '/buyOrder/buyOrder_index',
    alwaysShow: true,
    name: '',
    meta: { title: '采购订单管理', icon: 'icon8', identify: 'pop:buyOrder' },
    children: [
      {
        path: 'buyOrder_pre_index',
        name: 'BuyOrderPreIndex',
        meta: {
          title: '预采购订单',
          icon: '',
          identify: 'pop:buyOrder:pre_list'
        },
        component: () => import('@/subject/pop/views/buy_order/pre_list')
      },
      {
        path: 'buyOrder_index',
        name: 'BuyOrderIndex',
        meta: { title: '采购订单', icon: '', identify: 'pop:buyOrder:list' },
        component: () => import('@/subject/pop/views/buy_order/list')
      },
      {
        path: 'buyOrder_reject',
        name: 'BuyOrderReject',
        meta: { title: '退货单', icon: '', identify: 'pop:buyOrder:reject' },
        component: () => import('@/subject/pop/views/buy_order/reject_list')
      },
      {
        path: 'buyOrder_edit/:id',
        name: 'BuyOrderEdit',
        meta: { title: '订单详情', icon: '', identify: 'pop:buyOrder:detail', activeMenu: ['/buyOrder/buyOrder_index'], noCache: true },
        component: () => import('@/subject/pop/views/buy_order/detail'),
        hidden: true
      },
      {
        // 收货、退货
        path: 'buyOrder_receive/:id',
        name: 'BuyOrderReceive',
        meta: { title: '订单详情', icon: '', identify: 'pop:buyOrder:receive', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/pop/views/buy_order/receive'),
        hidden: true
      },
      {
        // 退货单详情
        path: 'buyOrder_reject_detail/:id',
        name: 'BuyOrderRejectDetail',
        meta: {
          title: '退货单详情',
          icon: '',
          identify: 'pop:buyOrder:reject_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/buy_order/reject_detail'),
        hidden: true
      },
      {
        // 预订单详情
        path: 'buyOrder_pre_detail/:id',
        name: 'BuyOrderPreDetail',
        meta: {
          title: '预订单详情',
          icon: '',
          identify: 'pop:buyOrder:pre_detail',
          activeMenu: ['/buyOrder/buyOrder_pre_index'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/buy_order/pre_detail'),
        hidden: true
      }
    ]
  }
]

export default route
