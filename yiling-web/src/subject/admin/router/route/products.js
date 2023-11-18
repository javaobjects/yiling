const route = [
  {
    path: '/products',
    redirect: '/products/products_index',
    name: '',
    meta: { title: '商品管理', icon: 'icon2', identify: 'sys:product' },
    children: [
      {
        path: 'products_index',
        name: 'ProductsIndex',
        meta: { title: '商品信息管理', icon: '', identify: 'sys:product:info' },
        component: () => import('@/subject/admin/views/products/index')
      },
      {
        path: 'products_detail/:id/:name',
        name: 'ProductsDetail',
        meta: { title: '商品详情', icon: '', identify: 'sys:product:detail', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/admin/views/products/detail/index'),
        hidden: true
      },
      {
        path: 'invite_price',
        name: 'InvitePrice',
        meta: { title: '招标挂网价管理', icon: '', identify: 'sys:product:invite_price' },
        component: () => import('@/subject/admin/views/products/invite_price')
      },
      {
        path: 'invite_price_detail/:id',
        name: 'InvitePriceDetail',
        meta: { title: '招标挂网价详情', icon: '', identify: 'sys:product:invite_price_detail', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/admin/views/products/invite_price_detail'),
        hidden: true
      }
    ]
  }
]

export default route
