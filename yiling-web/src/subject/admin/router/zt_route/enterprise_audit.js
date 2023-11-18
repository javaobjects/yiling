const route = [
  {
    path: '/enterprise_audit',
    alwaysShow: true,
    name: '',
    redirect: '/enterprise_audit/enterprise_audit_list',
    meta: { title: '企业审核', icon: 'icon22', identify: 'zt:enterprise_audit' },
    children: [
      {
        path: 'enterprise_audit_list',
        name: 'EnterpriseAuditList',
        meta: {
          title: '企业认证审核列表',
          icon: '',
          identify: 'zt:enterprise_audit:enterprise_audit_list'
        },
        component: () => import('@/subject/admin/views_zt/enterprise_audit/index')
      },
      {
        path: 'to_examine/:eid',
        name: 'ToExamine',
        meta: {
          title: '审核详情',
          icon: '',
          identify: 'zt:enterprise_audit:to_examine',
          activeMenu: ['/enterprise_audit/enterprise_audit_list'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/enterprise_audit/to_examine/index'),
        hidden: true
      },
      {
        path: 'to_examine_two/:eid',
        name: 'ToExamineTwo',
        meta: {
          title: '审核详情',
          icon: '',
          identify: 'zt:enterprise_audit:to_examine_two',
          activeMenu: ['/enterprise_audit/enterprise_audit_list'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/enterprise_audit/to_examine_two/index'),
        hidden: true
      },
      {
        path: 'collection_account',
        name: 'CollectionAccount',
        meta: {
          title: '企业收款账户列表',
          icon: '',
          identify: 'zt:enterprise_audit:collection_account'
        },
        component: () => import('@/subject/admin/views_zt/enterprise_audit/collection_account/index')
      },
      {
        path: 'collection_account_toExamine/:id',
        name: 'CollectionAccountToExamine',
        meta: {
          title: '查看/修改',
          icon: '',
          identify: 'zt:enterprise_audit:collection_account_toExamine',
          activeMenu: ['/enterprise_audit/collection_account'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/enterprise_audit/collection_account/details/index'),
        hidden: true
      },
      {
        path: 'account_cancel_audit',
        name: 'AccountCancelAudit',
        meta: {
          title: '账号注销审核列表',
          icon: '',
          identify: 'zt:enterprise_audit:account_cancel_audit'
        },
        component: () => import('@/subject/admin/views_zt/enterprise_audit/account_cancel_audit/index')
      },
      {
        path: 'cancel_audit_detail/:id',
        name: 'CancelAuditDetail',
        meta: {
          title: '账号注销审核详情',
          icon: '',
          identify: 'zt:enterprise_audit:cancel_audit_detail',
          activeMenu: ['/enterprise_audit/account_cancel_audit'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/enterprise_audit/account_cancel_audit/cancel_audit_detail/index'),
        hidden: true
      }
    ]
  }
]
export default route
