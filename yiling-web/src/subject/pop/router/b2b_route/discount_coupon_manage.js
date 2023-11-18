const route = [
  {
    path: '/discount_coupon',
    alwaysShow: true,
    redirect: '/discount_coupon/discount_coupon_list',
    name: '',
    meta: { title: '优惠券管理', icon: 'icon16', identify: 'b2b:discount_coupon' },
    children: [
      {
        path: 'discount_coupon_list',
        name: 'DiscountCouponList',
        meta: {
          title: '优惠券活动',
          icon: '',
          identify: 'b2b:discount_coupon:discount_coupon_list'
        },
        component: () => import('@/subject/pop/views_b2b/discount_coupon_manage/discount_coupon_list')
      },
      {
        path: 'add_discount_coupon/:operationType?/:id?',
        name: 'AddDiscountCoupon',
        meta: {
          title: '添加优惠券',
          icon: '',
          identify: 'b2b:discount_coupon:add_discount_coupon',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/discount_coupon_manage/add_discount_coupon'),
        hidden: true
      }
    ]
  }
]

export default route
