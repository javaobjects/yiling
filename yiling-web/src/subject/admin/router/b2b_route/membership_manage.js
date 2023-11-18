const route = [
  {
    path: '/membership',
    redirect: '/membership/membership_list',
    name: '',
    meta: { title: '会员管理', icon: 'icon20', identify: 'b2b:membership' },
    children: [
      {
        path: 'membership_list',
        name: 'MembershipList',
        meta: {
          title: '会员卡信息',
          icon: '',
          identify: 'b2b:membership:membership_list'
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/membership_list')
      },
      {
        path: 'membership_edit/:id?',
        name: 'MembershipEdit',
        meta: {
          title: '创建会员',
          icon: '',
          identify: 'b2b:membership:membership_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/membership_edit'),
        hidden: true
      },
      {
        path: 'member_rights_list',
        name: 'MemberRightsList',
        meta: {
          title: '会员权益',
          icon: '',
          identify: 'b2b:membership:member_rights_list'
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/member_rights_list')
      },
      {
        path: 'member_rights_edit/:id?',
        name: 'MemberRightsEdit',
        meta: {
          title: '新建权益',
          icon: '',
          identify: 'b2b:membership:member_rights_list',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/member_rights_edit'),
        hidden: true
      },
      {
        path: 'buy_member_list',
        name: 'BuyMemberList',
        meta: {
          title: '已经购买会员记录',
          icon: '',
          identify: 'b2b:membership:buy_member_list'
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/buy_member_list')
      },
      {
        path: 'review_member_list',
        name: 'ReviewMemberList',
        meta: {
          title: '会员退款',
          icon: '',
          identify: 'b2b:membership:review_member_list'
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/review_member_list')
      },
      {
        path: 'member_query',
        name: 'MemberQuery',
        meta: {
          title: '会员查询',
          icon: '',
          identify: 'b2b:membership:member_query'
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/member_query')
      },
      {
        path: 'member_query_details/:id',
        name: 'MemberQueryDetails',
        meta: {
          title: '会员详情',
          icon: '',
          identify: 'b2b:membership:member_query_details',
          activeMenu: ['/membership/member_query'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/member_query/details'),
        hidden: true
      },
      {
        path: 'cancel_member_list',
        name: 'CancelMemberList',
        meta: {
          title: '取消会员查看',
          icon: '',
          identify: 'b2b:membership:cancel_member_list'
        },
        component: () => import('@/subject/admin/views_b2b/membership_manager/cancel_member_list')
      }
    ]
  }
]

export default route
