const route = [
  {
    path: '/establish_purchase',
    alwaysShow: true,
    redirect: '/establish_purchase/purchase_manage',
    name: '',
    meta: {
      title: '建采管理',
      icon: 'establish_purchase',
      identify: 'pop:establish_purchase',
      roles: []
    },
    children: [
      {
        path: 'purchase_manage',
        name: 'PurchaseManage',
        meta: {
          title: '建采管理',
          icon: '',
          identify: 'pop:establish_purchase:purchase_manage'
        },
        component: () => import('@/subject/pop/views/establish_purchase/purchase_manage/purchase_manage_list/index')
      },
      {
        path: 'purchase_manage_add',
        name: 'PurchaseManageAdd',
        meta: {
          title: '新增',
          icon: '',
          identify: 'pop:establish_purchase:purchase_manage_add',
          activeMenu: ['/establish_purchase/purchase_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/establish_purchase/purchase_manage/purchase_manage_add/index'),
        hidden: true
      },
      {
        path: 'purchase_manage_edit/:id',
        name: 'PurchaseManageEdit',
        meta: {
          title: '编辑',
          icon: '',
          identify: 'pop:establish_purchase:purchase_manage_edit',
          activeMenu: ['/establish_purchase/purchase_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/establish_purchase/purchase_manage/purchase_manage_edit/index'),
        hidden: true
      },
      {
        path: 'purchase_manage_detail/:id',
        name: 'PurchaseManageDetail',
        meta: {
          title: '详情',
          icon: '',
          identify: 'pop:establish_purchase:purchase_manage_detail',
          activeMenu: ['/establish_purchase/purchase_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/establish_purchase/purchase_manage/purchase_manage_detail/index'),
        hidden: true
      },
      {
        path: 'purchase_snapshot_detail/:id',
        name: 'PurchaseSnapshotDetail',
        meta: {
          title: '详情',
          icon: '',
          identify: 'pop:establish_purchase:purchase_snapshot_detail',
          activeMenu: ['/establish_purchase/purchase_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/establish_purchase/purchase_manage/purchase_snapshot_detail/index'),
        hidden: true
      },
      {
        path: 'commodity_template',
        name: 'CommodityTemplate',
        meta: {
          title: '商品模板',
          icon: '',
          identify: 'pop:establish_purchase:commodity_template'
        },
        component: () => import('@/subject/pop/views/establish_purchase/commodity_template')
      },
      {
        path: 'commodity_template_establish/:id',
        name: 'CommodityTemplateEstablish',
        meta: {
          title: '设置商品模板',
          icon: '',
          identify: 'pop:establish_purchase:commodity_template_establish',
          activeMenu: ['/establish_purchase/commodity_template'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/establish_purchase/commodity_template/establish'),
        hidden: true
      }
    ]
  }
]

export default route
