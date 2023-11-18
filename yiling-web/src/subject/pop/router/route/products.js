const route = [
  {
    path: '/products',
    alwaysShow: true,
    redirect: '/products/products_index',
    name: '',
    meta: { title: '商品管理', icon: 'icon2', identify: 'pop:product' },
    children: [
      {
        path: 'products_index',
        name: 'ProductsIndex',
        meta: { title: '商品列表', icon: '', identify: 'pop:product:index' },
        component: () => import('@/subject/pop/views/products/index')
      },
      {
        path: 'index_edit/:id',
        name: 'IndexEdit',
        meta: { title: '商品详情', icon: '', identify: 'pop:product:edit', activeMenu: ['/products/products_index'], noCache: true },
        component: () => import('@/subject/pop/views/products/detail/index'),
        hidden: true
      },
      {
        path: 'index_log/:id',
        name: 'IndexLog',
        meta: { title: '商品日志', icon: '', identify: 'pop:product:logs', activeMenu: ['/products/products_index'], noCache: true },
        component: () => import('@/subject/pop/views/products/logs/index'),
        hidden: true
      },
      {
        path: 'products_price',
        name: 'ProductsPrice',
        meta: { title: '商品定价', icon: '', identify: 'pop:product:price' },
        component: () => import('@/subject/pop/views/products/price/index')
      },
      {
        path: 'price_user/:id',
        name: 'PriceUser',
        meta: {
          title: '客户定价',
          icon: '',
          identify: 'pop:product:price_user',
          activeMenu: ['/products/products_price'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views/products/price_user/index'),
        hidden: true
      },
      {
        path: 'price_group/:id',
        name: 'PriceGroup',
        meta: {
          title: '分组定价',
          icon: '',
          identify: 'pop:product:price_group',
          activeMenu: ['/products/products_price'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views/products/price_group/index'),
        hidden: true
      },
      {
        path: 'oversell_products',
        name: 'OversellProducts',
        meta: { title: '超卖商品管理', icon: '', identify: 'pop:product:oversell' },
        component: () => import('@/subject/pop/views/products/oversell')
      },
      {
        path: 'products_group',
        name: 'ProductsGroup',
        meta: { title: '商品组合', icon: '', identify: 'pop:product:group' },
        component: () => import('@/subject/pop/views/products/goods_group/index')
      },
      {
        path: 'products_group_edit/:id?',
        name: 'ProductsGroupEdit',
        meta: { title: '商品组合编辑', icon: '', identify: 'pop:product_group:edit', activeMenu: ['/products/products_group'], noCache: true },
        component: () => import('@/subject/pop/views/products/goods_group_edit/index'),
        hidden: true
      }
    ]
  }
]

export default route
