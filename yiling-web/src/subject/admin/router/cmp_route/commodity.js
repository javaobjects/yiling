const route = [
  {
    path: '/commodity',
    redirect: '/commodity/commodity_manage',
    name: 'Commodity',
    meta: { title: '商品管理', icon: 'cmp_commodity', identify: 'cmp:commodity' },
    children: [
      {
        path: 'commodity_manage',
        name: 'CommodityManage',
        meta: {
          title: '商品管理',
          icon: '',
          identify: 'cmp:commodity:commodity_manage'
        },
        component: () => import('@/subject/admin/views_cmp/commodity/commodity_manage')
      },
      {
        path: 'business/:id?/:ename',
        name: 'Business',
        meta: {
          title: '商家',
          icon: '',
          identify: 'cmp:commodity:business',
          activeMenu: ['/commodity/commodity_manage'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/commodity/business')
      }
    ]
  }
]
export default route