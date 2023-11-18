const route = [
    {
      path: '/cmp-products',
      alwaysShow: true,
      redirect: '/cmp-products/cmp_products_index',
      name: '',
      meta: { title: '商品管理cmp', icon: 'icon2', identify: 'cmp:product' },
      children: [
        {
          path: 'cmp_products_index',
          name: 'CmpProductsIndex',
          meta: { title: '商品列表', icon: '', identify: 'cmp:product:index' },
          component: () => import('@/subject/pop/views_cmp/products/index')
        }
      ]
    }
  ]
  
  export default route
  