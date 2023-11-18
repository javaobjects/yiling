const route = [
  {
    path: '/b2b_products',
    redirect: '/b2b_products/b2b_products_index',
    name: '',
    meta: { title: '商品管理', icon: 'icon2', identify: 'b2b:b2b_products' },
    children: [
      {
        path: 'b2b_products_index',
        name: 'B2bProductsIndex',
        meta: { 
          title: '商品信息管理', 
          icon: '', 
          identify: 'b2b:b2b_products:b2b_products_index' 
        },
        component: () => import('@/subject/admin/views_b2b/products/index')
      },
      {
        path: 'b2b_products_detail/:id/:name',
        name: 'B2bProductsDetail',
        meta: { title: '商品详情', icon: '', identify: 'b2b:b2b_products:b2b_products_detail', activeMenu: ['/b2b_products/b2b_products_index'], noCache: true },
        component: () => import('@/subject/admin/views_b2b/products/detail/index'),
        hidden: true
      }
      
    ]
  }
]

export default route
