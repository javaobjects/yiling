const route = [
  {
    path: '/resource_management',
    redirect: '/resource_management/point_auditList',
    alwaysShow: true,
    name: '',
    meta: { title: '资料审核', icon: 'icon21', identify: 'sale:resource_management' },
    children: [
      {
        path: 'resource_auditList',
        name: 'ResourceAuditList',
        meta: { title: '资料审核列表', icon: '', identify: 'sale:resource_management:resource_auditList'},
        component: () => import('@/subject/admin/views_sale/resource_management/auditList/index.vue')
      },
      {
        path: 'resource_auditList_detail/:id',
        name: 'ResourceAuditListDetail',
        meta: { 
          title: '审核详情', 
          icon: '', 
          identify: 'sale:resource_management:resource_auditList_detail', 
          activeMenu: ['/resource_management/resource_auditList'], 
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/resource_management/auditList/detail/index.vue'),
        hidden: true 
      },
      {
        path: 'flow_direction',
        name: 'FlowDirection',
        meta: { title: '流向查询', icon: '', identify: 'sale:resource_management:flow_direction'},
        component: () => import('@/subject/admin/views_sale/resource_management/flowDirection/index.vue')
      },
      {
        path: 'flow_direction_detail/:id',
        name: 'FlowDirectionDetail',
        meta: { 
          title: '流向详情', 
          icon: '', 
          identify: 'sale:resource_management:flow_direction_detail', 
          activeMenu: ['/resource_management/flow_direction'], 
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/resource_management/flowDirection/detail/index.vue'),
        hidden: true 
      }
    ]
  }
]

export default route
