const route = [
  {
    path: '/reconciliation',
    redirect: '/reconciliation/index',
    alwaysShow: true,
    name: '',
    meta: { title: '对账管理', icon: 'icon13', identify: 'sys:reconciliation' },
    children: [
      {
        path: 'reconciliation_entry',
        name: 'ReconciliationEntry',
        meta: { title: '企业返利入账申请', icon: '', identify: 'sys:reconciliation:reconciliation_entry'},
        component: () => import('@/subject/admin/views/reconciliation/reconciliation_entry/index')
      },
      {
        path: 'reconciliation_rebate',
        name: 'ReconciliationRebate',
        meta: {
          title: '企业返利对账',
          icon: '',
          identify: 'sys:reconciliation:reconciliation_rebate'

        },
        component: () => import('@/subject/admin/views/reconciliation/reconciliation_rebate/index')
      },
      {
        path: 'reconciliation_entry_info/:id',
        name: 'ReconciliationEntryInfo',
        meta: {
          title: '企业返利入账申请信息详情',
          icon: '',
          identify: 'sys:reconciliation:reconciliation_entry_info',
          activeMenu: ['/reconciliation/reconciliation_entry'],
          noCache: true
        },
        component: () => import('@/subject/admin/views/reconciliation/reconciliation_entry_info/index'),
        hidden: true
      },
      {
        path: 'reconciliation_rebate_info/:easCode/:eid',
        name: 'ReconciliationRebateInfo',
        meta: {
          title: '已使用金额详情',
          icon: '',
          identify: 'sys:reconciliation:reconciliation_rebate_info',
          activeMenu: ['/reconciliation/reconciliation_rebate'],
          noCache: true
        },
        component: () => import('@/subject/admin/views/reconciliation/reconciliation_rebate/reconciliation_rebate_info/index'),
        hidden: true
      },
      {
        path: 'reconciliation_rebate_application',
        name: 'ReconciliationRebateApplication',
        meta: {
          title: '申请入账返利信息',
          icon: '',
          identify: 'sys:reconciliation:reconciliation_rebate_application',
          activeMenu: ['/reconciliation/reconciliation_rebate'],
          noCache: true
        },
        component: () => import('@/subject/admin/views/reconciliation/reconciliation_rebate/reconciliation_rebate_application/index'),
        hidden: true
      }
    ]
  }
]

export default route
