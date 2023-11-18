const route = [
    {
      path: '/b2b_products',
      alwaysShow: true,
      redirect: '/b2b_products/products_list',
      name: '',
      meta: { title: '商品管理b2b', icon: 'icon2', identify: 'b2b:product' },
      children: [
        {
          path: 'products_list',
          name: 'B2BProductsList',
          meta: { title: '商品列表', icon: '', identify: 'b2b:product:index' },
          component: () => import('@/subject/pop/views_b2b/products/index')
        },
        {
          path: 'products_edit/:id',
          name: 'ProductsEdit',
          meta: { title: '商品详情', icon: '', identify: 'b2b:product:product_detail', activeMenu: ['/b2b_products/products_list'] },
          component: () => import('@/subject/pop/views_b2b/products/detail/index'),
          hidden: true
        },
        {
          path: 'products_set_price/:id',
          name: 'ProductsSetPrice',
          meta: { title: '限价详情页', icon: '', identify: 'b2b:product:product_set_price_detail', activeMenu: ['/b2b_products/products_list'] },
          component: () => import('@/subject/pop/views_b2b/products/set_price/index'),
          hidden: true
        },
        {
          path: 'products_control_sale/:id',
          name: 'productsControlSale',
          meta: { title: '控销详情页', icon: '', identify: 'b2b:product:product_control_sale_detail', activeMenu: ['/b2b_products/products_list'] },
          component: () => import('@/subject/pop/views_b2b/products/control_sale/index'),
          hidden: true
        },
        {
          path: 'control_price_admin',
          name: 'ControlPriceAdmin',
          meta: { title: '控价管理', icon: '', identify: 'b2b:product:product_control_price' },
          component: () => import('@/subject/pop/views_b2b/control_price_admin/index')
        },
        {
          path: 'control_price_detail/:id',
          name: 'ControlPriceDetail',
          meta: { title: '控价详情页', icon: '', identify: 'b2b:product:product_control_price_detail', activeMenu: ['*'] },
          component: () => import('@/subject/pop/views_b2b/control_price_admin/detail/index'),
          hidden: true
        },
        {
          path: 'set_price_admin',
          name: 'SetPriceAdmin',
          meta: { title: '定价管理', icon: '', identify: 'b2b:product:product_set_price' },
          component: () => import('@/subject/pop/views_b2b/set_price_admin/index')
        },
        {
          path: 'set_customer_price_detail/:id',
          name: 'SetCustomerPriceDetail',
          meta: { title: '设置客户定价', icon: '', identify: 'b2b:product:product_customer_price_detail', activeMenu: ['*'] },
          component: () => import('@/subject/pop/views_b2b/set_price_admin/set_customer_price_detail/index.vue'),
          hidden: true
        },
        {
          path: 'set_customer_group_price_detail/:id',
          name: 'SetCustomerGroupPriceDetail',
          meta: { title: '设置客户分组定价', icon: '', identify: 'b2b:product:product_customer_group_price_detail', activeMenu: ['*'] },
          component: () => import('@/subject/pop/views_b2b/set_price_admin/set_customer_group_price_detail'),
          hidden: true
        },
        {
          path: 'purchase_restric',
          name: 'PurchaseRestric',
          meta: { title: '限购管理', icon: '', identify: 'b2b:product:purchase_restric' },
          component: () => import('@/subject/pop/views_b2b/purchase_restric/index')
        },
        {
          path: 'purchase_restric_detail/:id',
          name: 'PurchaseRestricDetail',
          meta: { 
            title: '编辑限购', 
            icon: '', 
            identify: 'b2b:product:purchase_restric_detail',
            activeMenu: ['/b2b_products/purchase_restric'],
            noCache: true
          },
          component: () => import('@/subject/pop/views_b2b/purchase_restric/detail/index'),
          hidden: true
        }
      ]
    }
  ]
  
  export default route
  