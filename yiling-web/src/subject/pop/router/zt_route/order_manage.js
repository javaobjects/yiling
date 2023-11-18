const route = [
  {
    path: '/zt_order_manage',
    redirect: '/zt_order_manage/zt_order_manage_list',
    name: '',
    meta: { title: '企业订单数据', icon: 'icon8', identify: 'zt:order_manage' },
    children: [
      {
        path: 'zt_order_manage_list',
        name: 'ZtOrderManageList',
        meta: {
          title: '企业订单',
          icon: '',
          identify: 'zt:order_manage:order_manage_list'
        },
        component: () => import('@/subject/pop/views_zt/order_manage/order_manage_list')
      },
      {
        path: 'zt_order_manage_detail/:id',
        name: 'ZtOrderManageDetail',
        meta: {
          title: '订单详情',
          icon: '',
          identify: 'zt:order_manage:order_manage_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_zt/order_manage/order_manage_detail'),
        hidden: true
      },
      {
        path: 'zt_goods_returned_list',
        name: 'ZtGoodsReturnedList',
        meta: {
          title: '企业退货单',
          icon: '',
          identify: 'zt:order_manage:goods_returned_list'
        },
        component: () => import('@/subject/pop/views_zt/order_manage/goods_returned_list')
      },
      {
        path: 'zt_goods_returned_detail/:id',
        name: 'ZtGoodsReturnedDetail',
        meta: {
          title: '退货单详情',
          icon: '',
          identify: 'zt:order_manage:goods_returned_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_zt/order_manage/goods_returned_detail'),
        hidden: true
      }
    ]
  }
]

export default route
