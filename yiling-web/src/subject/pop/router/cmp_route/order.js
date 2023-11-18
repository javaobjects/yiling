const route = [
  {
    path: '/cmp_order',
    alwaysShow: true,
    redirect: '/cmp_order/cmp_pending_order',
    name: '',
    meta: { title: '订单管理cmp', icon: 'icon25', identify: 'cmp:order' },
    children: [
      {
        path: 'cmp_pending_order',
        name: 'CmpPendingOrder',
        meta: { title: '药品待处理订单', icon: '', identify: 'cmp:order:pending_order' },
        component: () => import('@/subject/pop/views_cmp/order/pendingOrder/index')
      },
      {
        path: 'cmp_order_list',
        name: 'CmpOrderList',
        meta: { title: '药险兑付订单', icon: '', identify: 'cmp:order:order_list' },
        component: () => import('@/subject/pop/views_cmp/order/orderList/index')
      },
      {
        path: 'cmp_insurance_sale_record',
        name: 'CmpInsuranceSaleRecord',
        meta: { title: '保险销售记录', icon: '', identify: 'cmp:order:insurance_sale_record' },
        component: () => import('@/subject/pop/views_cmp/order/insuranceSaleRecord/index')
      },
      {
        path: 'cmp_return_order',
        name: 'CmpReturnOrder',
        meta: { title: '退货退单', icon: '', identify: 'cmp:order:return_order' },
        component: () => import('@/subject/pop/views_cmp/order/returnOrder/index')
      },
      {
        path: 'cmp_order_detail/:id/:type',
        name: 'CmpOrderDetail',
        meta: { 
          title: '订单详情', 
          icon: '', 
          identify: 'cmp:order:order_detail', 
          activeMenu: ['/cmp_order/cmp_order_list', '/cmp_order/cmp_pending_order', '/cmp_order/cmp_return_order'], 
          noCache: true
        },
        component: () => import('@/subject/pop/views_cmp/order/orderDetail/index'),
        hidden: true
      },
      {
        path: 'cmp_choice_send_goods/:id/:type',
        name: 'CmpChoiceSendGoods',
        meta: { 
          title: '备货发货', 
          icon: '', 
          identify: 'cmp:order:choice_send_goods', 
          activeMenu: ['/cmp_order/cmp_pending_order', '/cmp_order/cmp_order_list', '/cmp_order/cmp_insurance_sale_record'], 
          noCache: true
        },
        component: () => import('@/subject/pop/views_cmp/order/choiceSendGoods/index'),
        hidden: true
      },
      {
        path: 'cmp_insurance_order_detail/:id/:type',
        name: 'CmpInsuranceOrderDetail',
        meta: { 
          title: '保险订单详情', 
          icon: '', 
          identify: 'cmp:order:insurance_order_detail',
          activeMenu: ['/cmp_order/cmp_insurance_sale_record'], 
          noCache: true
        },
        component: () => import('@/subject/pop/views_cmp/order/insuranceOrderDetail/index'),
        hidden: true
      },
      {
        path: 'drug_welfare',
        name: 'DrugWelfare',
        meta: { 
          title: '药品福利计划', 
          icon: '', 
          identify: 'cmp:order:drug_welfare' 
        },
        component: () => import('@/subject/pop/views_cmp/order/drugWelfare/index')
      },
      {
        path: 'write_off_record',
        name: 'WriteOffRecord',
        meta: { 
          title: '核销记录', 
          icon: '', 
          identify: 'cmp:order:write_off_record',
          activeMenu: ['/cmp_order/drug_welfare'], 
          noCache: true
        },
        component: () => import('@/subject/pop/views_cmp/order/drugWelfare/writeOffRecord/index'),
        hidden: true
      },
      {
        path: 'cmp_goods_list',
        name: 'CmpGoodsList',
        meta: { title: '商城订单', icon: '', identify: 'cmp:order:goods_list' },
        component: () => import('@/subject/pop/views_cmp/order/goodsList/index')
      },
      {
        path: 'cmp_goods_detail/:id',
        name: 'CmpGoodsDetail',
        meta: { 
          title: '商城订单详情', 
          icon: '', 
          identify: 'cmp:order:goods_list_detail', 
          activeMenu: ['/cmp_order/cmp_goods_list'], 
          noCache: true
        },
        component: () => import('@/subject/pop/views_cmp/order/goodsDetail/index'),
        hidden: true
      }
    ]
  }
];

export default route;
