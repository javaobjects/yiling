const route = [
  {
    path: '/supplier',
    alwaysShow: true,
    name: '',
    redirect: '/supplier/supplier_list',
    meta: { 
      title: '供应商管理', 
      icon: 'supplier', 
      identify: 'zt:supplier' 
    },
    children: [
      {
        path: 'supplier_list',
        name: 'SupplierList',
        meta: { 
          title: '供应商列表', 
          icon: '', 
          identify: 'zt:supplier:supplier_list' 
        },
          component: () => import('@/subject/pop/views_zt/supplier/supplier_list/index')
      }
    ]
  }
]
export default route