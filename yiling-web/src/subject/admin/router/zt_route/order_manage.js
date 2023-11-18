const route = [
  {
    path: '/order_manage',
    redirect: '/order_manage/order_manage_list',
    name: '',
    meta: { title: '订单管理', icon: 'icon12', identify: 'zt:order_manage' },
    children: [
      {
        path: 'order_manage_list',
        name: 'OrderManageList',
        meta: {
          title: '订单列表',
          icon: '',
          identify: 'zt:order_manage:order_manage_list'
        },
        component: () => import('@/subject/admin/views_zt/order_manage/order_manage_list')
      },
      {
        path: 'order_manage_detail/:id',
        name: 'OrderManageDetail',
        meta: {
          title: '订单详情',
          icon: '',
          identify: 'pop:order_manage:order_manage_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/order_manage/order_manage_detail'),
        hidden: true
      },
      {
        path: 'goods_returned_list',
        name: 'GoodsReturnedList',
        meta: {
          title: '退货单',
          icon: '',
          identify: 'zt:order_manage:goods_returned_list'
        },
        component: () => import('@/subject/admin/views_zt/order_manage/goods_returned_list')
      },
      {
        path: 'goods_returned_detail/:id',
        name: 'GoodsReturnedDetail',
        meta: {
          title: '退货单详情',
          icon: '',
          identify: 'pop:order_manage:goods_returned_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/order_manage/goods_returned_detail'),
        hidden: true
      }
    ]
  }
]

export default route
