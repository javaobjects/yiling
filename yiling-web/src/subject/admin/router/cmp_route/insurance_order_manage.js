const route = [
  {
    path: '/insurance_order_manage',
    redirect: '/insurance_order_manage/insurance_sell_order',
    name: '',
    meta: { title: '保险订单管理', icon: 'insurance_order_manage', identify: 'cmp:insure_order_manage' },
    children: [
      {
        path: 'insurance_sell_order',
        name: 'InsuranceSellOrder',
        meta: { title: '保险销售单', icon: '', identify: 'cmp:insure_order_manage:insurance_sell_order' },
        component: () => import('@/subject/admin/views_cmp/insurance_order_manage/insurance_sell_order/insurance_sell_list/index')
      },
      {
        path: 'insurance_sell_detail/:type/:id',
        name: 'InsuranceSellDetail',
        meta: { title: '保险销售单详情', icon: '', identify: 'cmp:insure_order_manage:insurance_sell_detail', activeMenu: ['/insurance_order_manage/insurance_sell_order'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_order_manage/insurance_sell_order/insurance_sell_detail/index'),
        hidden: true
      },
      {
        path: 'insurance_goods_order',
        name: 'InsuranceGoodsOrder',
        meta: { title: '药险兑付订单', icon: '', identify: 'cmp:insure_order_manage:insurance_goods_order' },
        component: () => import('@/subject/admin/views_cmp/insurance_order_manage/insurance_goods_order/goods_order_list/index')
      },
      {
        path: 'goods_order_detail/:id',
        name: 'GoodsOrderDetail',
        meta: { title: '保险商品订单', icon: '', identify: 'cmp:insure_order_manage:goods_order_detail', activeMenu: ['/insurance_order_manage/insurance_goods_order'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_order_manage/insurance_goods_order/goods_order_detail/index'),
        hidden: true
      },
      {
        path: 'insurance_goods_list',
        name: 'InsuranceGoodsList',
        meta: { title: '商城订单', icon: '', identify: 'cmp:insure_order_manage:insurance_goods_list' },
        component: () => import('@/subject/admin/views_cmp/insurance_order_manage/insurance_goods_order/goods_list/index')
      },
      {
        path: 'insurance_goods_list_detail/:id',
        name: 'InsuranceGoodsListDetail',
        meta: { 
          title: '商城订单详情', 
          icon: '', 
          identify: 'cmp:insure_order_manage:insurance_goods_list_detail', 
          activeMenu: ['/insurance_order_manage/insurance_goods_list'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_cmp/insurance_order_manage/insurance_goods_order/goods_list_detail/index'),
        hidden: true
      }
    ]
  }
]

export default route