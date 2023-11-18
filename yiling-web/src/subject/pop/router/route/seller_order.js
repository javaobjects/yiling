const route = [
  {
    path: '/sell_order',
    redirect: '/sell_order/sell_order_list',
    alwaysShow: true,
    name: '',
    meta: { title: '销售订单管理', icon: 'icon7', identify: 'pop:sell_order' },
    children: [
      {
        path: 'sell_order_list',
        name: 'SellOrderList',
        meta: {
          title: '销售订单',
          icon: '',
          identify: 'pop:sell_order:sell_order_list'
        },
        component: () => import('@/subject/pop/views/sell_order/sell_order_list')
      },
      {
        path: 'sell_order_send/:id',
        name: 'SellOrderSend',
        meta: {
          title: '发货界面',
          icon: '',
          identify: 'pop:sell_order:sell_order_send',
          activeMenu: ['/sell_order/sell_order_list']
        },
        component: () => import('@/subject/pop/views/sell_order/sell_order_send'),
        hidden: true
      },
      {
        path: 'sell_order_detail/:id',
        name: 'SellOrderDetail',
        meta: {
          title: '订单详情',
          icon: '',
          identify: 'pop:sell_order:sell_order_detail',
          activeMenu: ['/sell_order/sell_order_list', '/sell_order/invoice_list']
        },
        component: () => import('@/subject/pop/views/sell_order/sell_order_detail'),
        hidden: true
      },
      {
        path: 'invoice_list',
        name: 'InvoiceList',
        meta: {
          title: '发票管理',
          icon: '',
          identify: 'pop:sell_order:invoice_list'
        },
        component: () => import('@/subject/pop/views/sell_order/invoice_list')
      },
      {
        path: 'invoice_apply/:id/:type',
        name: 'InvoiceApply',
        meta: {
          title: '发票申请',
          icon: '',
          identify: 'pop:sell_order:invoice_apply',
          activeMenu: ['/sell_order/invoice_list']
        },
        component: () => import('@/subject/pop/views/sell_order/invoice_apply'),
        hidden: true
      },
      {
        path: 'invoice_info_list/:id/',
        name: 'InvoiceInfoList',
        meta: {
          title: '发票列表',
          icon: '',
          identify: 'pop:sell_order:invoice_info_list',
          activeMenu: ['/sell_order/invoice_list']
        },
        component: () => import('@/subject/pop/views/sell_order/invoice_info_list'),
        hidden: true
      },
      {
        path: 'invoice_info_Detail/:applyId/',
        name: 'InvoiceInfoDetail',
        meta: {
          title: '发票详情',
          icon: '',
          identify: 'pop:sell_order:invoice_info_detail',
          activeMenu: ['/sell_order/invoice_list']
        },
        component: () => import('@/subject/pop/views/sell_order/invoice_info_detail'),
        hidden: true
      },
      {
        path: 'goods_return_list',
        name: 'GoodsReturnList',
        meta: {
          title: '退货单',
          icon: '',
          identify: 'pop:sell_order:goods_return_list'
        },
        component: () => import('@/subject/pop/views/sell_order/goods_return_list')
      },
      {
        path: 'goods_return_detail/:id',
        name: 'GoodsReturnDetail',
        meta: {
          title: '退货单详情',
          icon: '',
          identify: 'pop:sell_order:goods_return_detail',
          activeMenu: ['/sell_order/goods_return_list']
        },
        component: () => import('@/subject/pop/views/sell_order/goods_return_detail'),
        hidden: true
      },
      {
        path: 'extend_sale_order',
        name: 'ExtendSaleOrder',
        meta: {
          title: '数拓销售订单',
          icon: '',
          identify: 'pop:sell_order:extend_sale_order'
        },
        component: () => import('@/subject/pop/views/sell_order/extend_sale_order/order_list')
      },
      {
        path: 'extend_sale_order_detail/:id',
        name: 'ExtendSaleOrderDetail',
        meta: {
          title: '数拓销售订单详情',
          icon: '',
          identify: 'pop:sell_order:extend_sale_order_detail',
          activeMenu: ['/sell_order/extend_sale_order'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/extend_sale_order/order_detail'),
        hidden: true
      },
      {
        path: 'extend_sale_order_send/:id',
        name: 'ExtendSaleOrderSend',
        meta: {
          title: '数拓销售订单详情(发货)',
          icon: '',
          identify: 'pop:sell_order:extend_sale_order_send',
          activeMenu: ['/sell_order/extend_sale_order'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/extend_sale_order/order_send'),
        hidden: true
      },
      {
        path: 'distribute_sale_order',
        name: 'DistributeSaleOrder',
        meta: {
          title: '分销销售订单',
          icon: '',
          identify: 'pop:sell_order:distribute_sale_order'
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_sale_order/order_list')
      },
      {
        path: 'distribute_sale_order_detail/:id',
        name: 'DistributeSaleOrderDetail',
        meta: {
          title: '分销销售订单详情',
          icon: '',
          identify: 'pop:sell_order:distribute_sale_order_detail',
          activeMenu: ['/sell_order/distribute_sale_order'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_sale_order/order_detail'),
        hidden: true
      },
      {
        path: 'distribute_sale_order_send/:id',
        name: 'DistributeSaleOrderSend',
        meta: {
          title: '分销销售订单详情(发货)',
          icon: '',
          identify: 'pop:sell_order:distribute_sale_order_send',
          activeMenu: ['/sell_order/distribute_sale_order'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_sale_order/order_send'),
        hidden: true
      },
      {
        path: 'extend_invoice_manage',
        name: 'ExtendInvoiceManage',
        meta: {
          title: '数拓发票管理',
          icon: '',
          identify: 'pop:sell_order:extend_invoice_manage'
        },
        component: () => import('@/subject/pop/views/sell_order/extend_invoice_manage/invoice_list')
      },
      {
        path: 'extend_invoice_info_list/:id',
        name: 'ExtendInvoiceInfoList',
        meta: {
          title: '数拓发票列表',
          icon: '',
          identify: 'pop:sell_order:extend_invoice_info_list',
          activeMenu: ['/sell_order/extend_invoice_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/extend_invoice_manage/invoice_info_list'),
        hidden: true
      },
      {
        path: 'extend_invoice_info_detail/:applyId',
        name: 'ExtendInvoiceInfoDetail',
        meta: {
          title: '数拓发票详情',
          icon: '',
          identify: 'pop:sell_order:extend_invoice_info_detail',
          activeMenu: ['/sell_order/extend_invoice_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/extend_invoice_manage/invoice_info_list/invoice_info_detail'),
        hidden: true
      },
      {
        path: 'extend_invoice_apply/:id/:type',
        name: 'ExtendInvoiceApply',
        meta: {
          title: '数拓申请发票',
          icon: '',
          identify: 'pop:sell_order:extend_invoice_apply',
          activeMenu: ['/sell_order/extend_invoice_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/extend_invoice_manage/invoice_apply'),
        hidden: true
      },
      {
        path: 'distribute_invoice_manage',
        name: 'DistributeInvoiceManage',
        meta: {
          title: '分销发票管理',
          icon: '',
          identify: 'pop:sell_order:distribute_invoice_manage'
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_invoice_manage/invoice_list')
      },
      {
        path: 'distribute_invoice_info_list/:id',
        name: 'DistributeInvoiceInfoList',
        meta: {
          title: '分销发票列表',
          icon: '',
          identify: 'pop:sell_order:distribute_invoice_info_list',
          activeMenu: ['/sell_order/distribute_invoice_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_invoice_manage/invoice_info_list'),
        hidden: true
      },
      {
        path: 'distribute_invoice_info_detail/:applyId',
        name: 'DistributeInvoiceInfoDetail',
        meta: {
          title: '分销发票详情',
          icon: '',
          identify: 'pop:sell_order:distribute_invoice_info_detail',
          activeMenu: ['/sell_order/distribute_invoice_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_invoice_manage/invoice_info_list/invoice_info_detail'),
        hidden: true
      },
      {
        path: 'distribute_invoice_apply/:id/:type',
        name: 'DistributeInvoiceApply',
        meta: {
          title: '分销申请发票',
          icon: '',
          identify: 'pop:sell_order:distribute_invoice_apply',
          activeMenu: ['/sell_order/distribute_invoice_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_invoice_manage/invoice_apply'),
        hidden: true
      },
      {
        path: 'extend_return_order',
        name: 'ExtendReturnOrder',
        meta: {
          title: '数拓退货单',
          icon: '',
          identify: 'pop:sell_order:extend_return_order'
        },
        component: () => import('@/subject/pop/views/sell_order/extend_return_order/order_list')
      },
      {
        path: 'extend_return_order_detail/:id',
        name: 'ExtendReturnOrderDetail',
        meta: {
          title: '数拓退货单详情',
          icon: '',
          identify: 'pop:sell_order:extend_return_order_detail',
          activeMenu: ['/sell_order/extend_return_order'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/extend_return_order/order_detail'),
        hidden: true
      },
      {
        path: 'distribute_return_order',
        name: 'DistributeReturnOrder',
        meta: {
          title: '分销退货单',
          icon: '',
          identify: 'pop:sell_order:distribute_return_order'
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_return_order/order_list')
      },
      {
        path: 'distribute_return_order_detail/:id',
        name: 'DistributeReturnOrderDetail',
        meta: {
          title: '分销退货单详情',
          icon: '',
          identify: 'pop:sell_order:distribute_return_order_detail',
          activeMenu: ['/sell_order/distribute_return_order'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/sell_order/distribute_return_order/order_detail'),
        hidden: true
      }
    ]
  }
]

export default route
