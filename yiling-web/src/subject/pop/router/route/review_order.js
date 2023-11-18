const route = [
  {
    path: '/reviewOrder',
    redirect: '/reviewOrder/reviewOrder_index',
    alwaysShow: true,
    name: '',
    meta: {
      title: '订单审核管理',
      icon: 'icon12',
      identify: 'pop:reviewOrder'
    },
    children: [
      {
        path: 'reviewOrder_index',
        name: 'ReviewOrderIndex',
        meta: { title: '订单审核列表', icon: '', identify: 'pop:reviewOrder:list' },
        component: () => import('@/subject/pop/views/review_order/list')
      },
      {
        path: 'reviewOrder_detail/:id/:type',
        name: 'ReviewOrderDetail',
        meta: {
          title: '订单审核详情',
          icon: '',
          identify: 'pop:reviewOrder:detail',
          activeMenu: ['/reviewOrder/reviewOrder_index'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/review_order/detail'),
        hidden: true
      },
      {
        path: 'wanOrder_index',
        name: 'WanOrderIndex',
        meta: { title: '万州订单审核', icon: '', identify: 'pop:reviewOrder:wanOrder_index' },
        component: () => import('@/subject/pop/views/review_order/wanOrder')
      },
      {
        path: 'wanOrder_detail/:id/:type',
        name: 'WanOrderDetail',
        meta: {
          title: '订单审核详情',
          icon: '',
          identify: 'pop:reviewOrder:wanOrder_detail',
          activeMenu: ['/reviewOrder/wanOrder_index'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/review_order/wanOrderDetail'),
        hidden: true
      },
      {
        path: 'extend_order_review',
        name: 'ExtendOrderReview',
        meta: {
          title: '数拓订单审核',
          icon: '',
          identify: 'pop:reviewOrder:extend_order_review'
        },
        component: () => import('@/subject/pop/views/review_order/extend_order_review/order_list')
      },
      {
        path: 'extend_order_detail/:id/:type',
        name: 'ExtendOrderDetail',
        meta: {
          title: '数拓审核详情',
          icon: '',
          identify: 'pop:reviewOrder:extend_order_detail',
          activeMenu: ['/reviewOrder/extend_order_review'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/review_order/extend_order_review/order_detail'),
        hidden: true
      },
      {
        path: 'distribute_order_review',
        name: 'DistributeOrderReview',
        meta: {
          title: '分销订单审核',
          icon: '',
          identify: 'pop:reviewOrder:distribute_order_review'
        },
        component: () => import('@/subject/pop/views/review_order/distribute_order_review/order_list')
      },
      {
        path: 'distribute_order_detail/:id/:type',
        name: 'DistributeOrderDetail',
        meta: {
          title: '分销审核详情',
          icon: '',
          identify: 'pop:reviewOrder:distribute_order_detail',
          activeMenu: ['/reviewOrder/distribute_order_review'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/review_order/distribute_order_review/order_detail'),
        hidden: true
      }
    ]
  }
]

export default route
