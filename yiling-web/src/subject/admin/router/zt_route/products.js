const route = [
  {
    path: '/zt_products',
    redirect: '/zt_products/zt_products_index',
    name: '',
    meta: { title: '商品管理', icon: 'icon2', identify: 'zt:zt_products' },
    children: [
      {
        path: 'zt_products_index',
        name: 'ZtProductsIndex',
        meta: { 
          title: '商品信息管理', 
          icon: '', 
          identify: 'zt:zt_products:zt_products_index' 
        },
        component: () => import('@/subject/admin/views_zt/products/index')
      },
      {
        path: 'zt_products_detail/:id/:name',
        name: 'ZtProductsDetail',
        meta: { title: '商品详情', icon: '', identify: 'zt:zt_products:zt_products_detail', activeMenu: ['/zt_products/zt_products_index'], noCache: true },
        component: () => import('@/subject/admin/views_zt/products/detail/index'),
        hidden: true
      }
      
    ]
  }
]

export default route
