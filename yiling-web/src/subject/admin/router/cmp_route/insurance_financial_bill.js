const route = [
  {
    path: '/insurance_financial_bill',
    redirect: '/insurance_financial_bill/financial_center',
    name: '',
    meta: { title: '保险财务账单', icon: 'insurance_financial_bill', identify: 'cmp:insure_financial_bill' },
    children: [
      {
        path: 'financial_center',
        name: 'FinancialCenter',
        meta: { title: '财务中心', icon: '', identify: 'cmp:insure_financial_bill:financial_center' },
        component: () => import('@/subject/admin/views_cmp/insurance_financial_bill/financial_center/index')
      },
      {
        path: 'settlement_bill/:id/:eid',
        name: 'SettlementBill',
        meta: { title: '终端结算账单', icon: '', identify: 'cmp:insure_financial_bill:settlement_bill', activeMenu: ['/insurance_financial_bill/financial_center'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_financial_bill/settlement_bill/index'),
        hidden: true
      },
      {
        path: 'reconciliation',
        name: 'Reconciliation',
        meta: { 
          title: '理赔对帐', 
          icon: '', 
          identify: 'cmp:insure_financial_bill:reconciliation'
        },
        component: () => import('@/subject/admin/views_cmp/insurance_financial_bill/reconciliation/index')
      }
    ]
  }
]

export default route