const route = [
  {
    path: '/goods_presell',
    redirect: '/goods_presell/b2b_presell_list',
    name: '',
    meta: { title: '商品预售', icon: 'goods_presell', identify: 'marketing:goods_presell' },
    children: [
      {
        path: 'b2b_presell_list',
        name: 'B2bPresellList',
        meta: {
          title: 'B2B预售',
          icon: '',
          identify: 'marketing:goods_presell:b2b_presell_list'
        },
        component: () => import('@/subject/admin/views_marketing/goods_presell/b2b_presell_list')
      },
      {
        path: 'presell_edit/:operationType/:id?',
        name: 'PresellEdit',
        meta: {
          title: '添加商品预售',
          icon: '',
          identify: 'marketing:goods_presell:presell_edit',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_marketing/goods_presell/presell_edit'),
        hidden: true
      }
    ]
  }
]

export default route
