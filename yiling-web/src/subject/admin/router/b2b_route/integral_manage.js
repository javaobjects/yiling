const route = [
  {
    path: '/integral_manage',
    alwaysShow: true,
    name: '',
    redirect: '/integral_manage/grant_manage',
    meta: { title: '积分管理', icon: 'icon21', identify: 'b2b:integral_manage' },
    children: [
      {
        path: 'grant_manage',
        name: 'GrantManage',
        meta: {
          title: '积分发放管理',
          icon: '',
          identify: 'b2b:integral_manage:grant_manage'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/grant_manage')
      },
      {
        path: 'consume_manage',
        name: 'ConsumeManage',
        meta: {
          title: '积分消耗管理',
          icon: '',
          identify: 'b2b:integral_manage:consume_manage'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/consume_manage')
      },
      {
        path: 'points_redemption',
        name: 'PointsRedemption',
        meta: {
          title: '积分兑换商品管理',
          icon: '',
          identify: 'b2b:integral_manage:points_redemption'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/points_redemption')
      },
      {
        path: 'grant_manage_establish/:type?/:id',
        name: 'GrantManageEstablish',
        meta: {
          title: '发放规则',
          icon: '',
          identify: 'b2b:integral_manage:grant_manage_establish',
          activeMenu: ['/integral_manage/grant_manage'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/grant_manage/establish'),
        hidden: true
      },
      {
        path: 'consume_manage_establish/:type?/:id',
        name: 'ConsumeManageEstablish',
        meta: {
          title: '消耗规则',
          icon: '',
          identify: 'b2b:integral_manage:consume_manage_establish',
          activeMenu: ['/integral_manage/consume_manage'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/consume_manage/establish'),
        hidden: true
      },
      {
        path: 'points_redemption_establish/:type?/:id',
        name: 'PointsRedemptionEstablish',
        meta: {
          title: '添加/查看/编辑商品',
          icon: '',
          identify: 'b2b:integral_manage:points_redemption_establish',
          activeMenu: ['/integral_manage/points_redemption'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/points_redemption/establish'),
        hidden: true
      },
      {
        path: 'points_distribution',
        name: 'PointsDistribution',
        meta: {
          title: '积分发放记录',
          icon: '',
          identify: 'b2b:integral_manage:points_distribution'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/points_distribution')
      },
      {
        path: 'points_deduction',
        name: 'PointsDeduction',
        meta: {
          title: '积分扣减记录',
          icon: '',
          identify: 'b2b:integral_manage:points_deduction'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/points_deduction')
      },
      {
        path: 'points_redemption_order',
        name: 'PointsRedemptionOrder',
        meta: {
          title: '积分兑换订单',
          icon: '',
          identify: 'b2b:integral_manage:points_redemption_order'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/points_redemption_order')
      },
      {
        path: 'integral_configuration',
        name: 'IntegralConfiguration',
        meta: {
          title: '积分说明配置',
          icon: '',
          identify: 'b2b:integral_manage:integral_configuration'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/integral_configuration')
      },
      {
        path: 'integral_news',
        name: 'IntegralNews',
        meta: {
          title: '积分兑换消息配置',
          icon: '',
          identify: 'b2b:integral_manage:integral_news'
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/integral_news')
      },
      {
        path: 'integral_news_establish/:id',
        name: 'IntegralNewsEstablish',
        meta: {
          title: '添加/编辑消息',
          icon: '',
          identify: 'b2b:integral_manage:integral_news_establish',
          activeMenu: ['/integral_manage/integral_news'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/integral_manage/integral_news/establish'),
        hidden: true
      }
    ]
  }
]
export default route