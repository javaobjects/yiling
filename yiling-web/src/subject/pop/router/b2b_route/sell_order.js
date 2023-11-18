const route = [
  {
    path: '/b2b_sell_order',
    redirect: '/b2b_sell_order/b2b_sell_order_list',
    alwaysShow: true,
    name: '',
    meta: { title: '销售订单b2b', icon: 'icon7', identify: 'b2b:sell_order' },
    children: [
      {
        path: 'b2b_sell_order_list',
        name: 'B2bSellOrderList',
        meta: {
          title: '销售订单',
          icon: '',
          identify: 'b2b:sell_order:sell_order_list'
        },
        component: () =>
          import('@/subject/pop/views_b2b/sell_order/sell_order_list')
      },
      {
        path: 'b2b_sell_order_send/:id',
        name: 'B2bSellOrderSend',
        meta: {
          title: '发货界面',
          icon: '',
          identify: 'b2b:sell_order:sell_order_send',
          activeMenu: ['*'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/sell_order/sell_order_send'),
        hidden: true
      },
      {
        path: 'b2b_sell_order_detail/:id',
        name: 'B2bSellOrderDetail',
        meta: {
          title: '订单详情',
          icon: '',
          identify: 'b2b:sell_order:sell_order_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/sell_order/sell_order_detail'),
        hidden: true
      },
      {
        path: 'b2b_goods_return_list',
        name: 'B2bGoodsReturnList',
        meta: {
          title: '退货单',
          icon: '',
          identify: 'b2b:sell_order:goods_return_list'
        },
        component: () =>
          import('@/subject/pop/views_b2b/sell_order/goods_return_list')
      },
      {
        path: 'b2b_goods_return_detail/:id',
        name: 'B2bGoodsReturnDetail',
        meta: {
          title: '退货单详情',
          icon: '',
          identify: 'b2b:sell_order:goods_return_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views_b2b/sell_order/goods_return_detail'),
        hidden: true
      }
    ]
  }
]

export default route
