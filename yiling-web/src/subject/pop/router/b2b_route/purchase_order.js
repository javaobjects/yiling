const route = [
  {
    path: '/b2b_purchase_order',
    redirect: '/b2b_purchase_order/b2b_purchase_order_list',
    alwaysShow: true,
    name: '',
    meta: { title: '采购订单pc', icon: 'icon8', identify: 'b2b:purchase_order' },
    children: [
      {
        path: 'b2b_purchase_order_list',
        name: 'B2bPurchaseOrderList',
        meta: {
          title: '采购订单',
          icon: '',
          identify: 'b2b:purchase_order:purchase_order_list'
        },
        component: () =>
          import('@/subject/pop/views_b2b/purchase_order/purchase_order_list')
      },
      {
        path: 'b2b_purchase_pay_status',
        name: 'B2bPurchasePayStaus',
        meta: {
          title: '支付',
          icon: '',
          identify: 'b2b:purchase_order:b2b_purchase_pay_status',
          activeMenu: ['/b2b_purchase_order/b2b_purchase_order_list'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/purchase_order/pay_status'),
        hidden: true
      },
      {
        path: 'b2b_purchase_order_detail/:id',
        name: 'B2bPurchaseOrderDetail',
        meta: {
          title: '订单详情',
          icon: '',
          identify: 'b2b:purchase_order:b2b_purchase_order_detail',
          activeMenu: ['/b2b_purchase_order/b2b_purchase_order_list'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/purchase_order/purchase_order_detail'),
        hidden: true
      },
      {
        path: 'b2b_purchase_order_receive/:type/:id',
        name: 'B2bPurchaseOrderReceive',
        meta: {
          title: '订单详情',
          icon: '',
          identify: 'b2b:purchase_order:b2b_purchase_order_receive',
          activeMenu: ['/b2b_purchase_order/b2b_purchase_order_list'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/purchase_order/purchase_receive'),
        hidden: true
      },
      {
        path: 'b2b_purchase_return_list',
        name: 'B2bPurchaseReturnList',
        meta: {
          title: '退货单',
          icon: '',
          identify: 'b2b:purchase_order:purchase_return_list'
        },
        component: () =>
          import('@/subject/pop/views_b2b/purchase_order/purchase_return_list')
      },
      {
        // 退货单详情
        path: 'b2b_purchase_return_detail/:id',
        name: 'B2bPurchaseReturnDetail',
        meta: {
          title: '退货单详情',
          icon: '',
          identify: 'b2b:purchase_order:purchase_return_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/purchase_order/purchase_return_detail'),
        hidden: true
      }
    ]
  }
]

export default route
