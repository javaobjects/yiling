const route = [
    {
      path: '/zt_products',
      alwaysShow: true,
      redirect: '/zt_products/products_list',
      name: '',
      meta: { title: '商品管理zt', icon: 'icon2', identify: 'zt:product' },
      children: [
        {
          path: 'products_list',
          name: 'ZtProductsList',
          meta: { title: '商品列表', icon: '', identify: 'zt:product:index' },
          component: () => import('@/subject/pop/views_zt/products/index')
        },
        {
            path: 'products_edit_detail/:id',
            name: 'ProductsEditDetail',
            meta: { title: '商品详情', icon: '', identify: 'zt:product:product_detail', activeMenu: ['/zt_products/products_list'], noCache: true},
            component: () => import('@/subject/pop/views_zt/products/products_edit_detail/index'),
            hidden: true
        },
        {
            path: 'products_again/:id',
            name: 'ZtProductsAgain',
            meta: { title: '重新提交', icon: '', identify: 'zt:product:product_again', activeMenu: ['/zt_products/products_list'], noCache: true},
            component: () => import('@/subject/pop/views_zt/products/products_again/index'),
            hidden: true
        },
        {
          path: 'products_add',
          name: 'ZtProductsAdd',
          meta: { title: '添加商品', icon: '', identify: 'zt:product:add', activeMenu: ['/zt_products/products_list'] },
          component: () => import('@/subject/pop/views_zt/products/products_add/index'),
          hidden: true
        },
        {
          path: 'create_products/:id',
          name: 'ZtCreateProducts',
          meta: { title: '新增商品', icon: '', identify: 'zt:product:create_product', activeMenu: ['/zt_products/products_list'], noCache: true},
          component: () => import('@/subject/pop/views_zt/products/create_products/index'),
          hidden: true
      }
      ]
    }
  ]
  
  export default route
  