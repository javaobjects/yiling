const route = [
  {
    path: '/discount_coupon',
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
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/discount_coupon_list')
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
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/add_discount_coupon'),
        hidden: true
      },
      {
        path: 'self_send_list',
        name: 'SelfSendList',
        meta: {
          title: '自动发放优惠券',
          icon: '',
          identify: 'b2b:discount_coupon:self_send_list'
        },
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/self_send_list')
      },
      {
        path: 'send_fail_list/:autoGiveId',
        name: 'SendFailList',
        meta: {
          title: '自动发券失败',
          icon: '',
          identify: 'b2b:discount_coupon:send_fail_list',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/send_fail_list'),
        hidden: true
      },
      {
        path: 'self_send_edit/:operationType?/:id?',
        name: 'SelfSendEdit',
        meta: {
          title: '添加自动发券',
          icon: '',
          identify: 'b2b:discount_coupon:self_send_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/self_send_edit'),
        hidden: true
      },
      {
        path: 'self_get_list',
        name: 'SelfGetList',
        meta: {
          title: '自动领取优惠券',
          icon: '',
          identify: 'b2b:discount_coupon:self_get_list'
        },
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/self_get_list')
      },
      {
        path: 'self_get_edit/:operationType?/:id?',
        name: 'SelfGetEdit',
        meta: {
          title: '添加自助领券',
          icon: '',
          identify: 'b2b:discount_coupon:self_get_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/self_get_edit'),
        hidden: true
      },
      {
        path: 'member_discount_coupon_list',
        name: 'MemberdiscountCouponList',
        meta: {
          title: '会员优惠券',
          icon: '',
          identify: 'b2b:discount_coupon:member_discount_coupon_list'
        },
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/member_discount_coupon_list')
      },
      {
        path: 'member_discount_coupon_edit/:operationType?/:id?',
        name: 'MemberdiscountCouponEdit',
        meta: {
          title: '添加会员优惠券',
          icon: '',
          identify: 'b2b:discount_coupon:member_discount_coupon_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/discount_coupon_manage/member_discount_coupon_edit'),
        hidden: true
      }
    ]
  }
]

export default route
