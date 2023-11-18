const route = [
  {
    path: '/sale_bussiness_auth_admin',
    redirect: '/sale_bussiness_auth_admin/sale_area',
    name: '',
    alwaysShow: true,
    meta: { title: '权限管理', icon: 'icon6', identify: 'sale:bussiness_auth_admin' },
    children: [
      {
        path: 'sale_area',
        name: 'SaleArea',
        meta: { title: '公司可售区域', icon: '', identify: 'sale:bussiness_auth_admin:sale_area', noCache: true },
        component: () => import('@/subject/pop/views_sale/bussiness_auth_admin/sale_area/index')
      },
      {
        path: 'sale_area_detail',
        name: 'SaleAreaDetail',
        meta: { title: '可售区域详情', icon: '', identify: 'sale:bussiness_auth_admin:sale_area_detail', noCache: true,
        activeMenu: ['/sale_bussiness_auth_admin/sale_area'] },
        component: () => import('@/subject/pop/views_sale/bussiness_auth_admin/sale_area_detail/index'),
        hidden: true
      }
    ]
  }
]

export default route
