const route = [
  {
    path: '/financial',
    alwaysShow: true,
    name: '',
    redirect: '/financial/collection_account',
    meta: { title: '财务管理', icon: 'icon23', identify: 'b2b:financial' },
    children: [
      {
        path: 'collection_account',
        name: 'CollectionAccount',
        meta: {
          title: '企业收款账户',
          icon: '',
          identify: 'b2b:financial:collection_account'
          
        },
        component: () => import('@/subject/pop/views_b2b/financial/collection_account')
      },
      {
        path: 'add_collection_account/:type',
        name: 'AddCollectionAccount',
        meta: {
          title: '添加收款账号',
          icon: '',
          identify: 'b2b:financial:add_collection_account',
          activeMenu: ['/financial/collection_account'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/financial/collection_account/add_collection_account'),
        hidden: true
      },
      {
        path: 'settlement_sheet',
        name: 'SettlementSheet',
        meta: {
          title: '结算单对账',
          icon: '',
          identify: 'b2b:financial:settlement_sheet'
        },
        component: () => import('@/subject/pop/views_b2b/financial/settlement_sheet')
      },
      {
        path: 'settlement_sheet_detailed/:id',
        name: 'SettlementSheetDetailed',
        meta: {
          title: '结算单对账详情',
          icon: '',
          identify: 'b2b:financial:settlement_sheet_detailed',
          activeMenu: ['/financial/settlement_sheet'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/financial/settlement_sheet/detailed'),
        hidden: true
      },
      {
        path: 'order_information/:orderId?/:settlementId',
        name: 'OrderInformation',
        meta: {
          title: '订单商品明细',
          icon: '',
          identify: 'b2b:financial:order_information',
          activeMenu: ['/financial/settlement_sheet'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/financial/settlement_sheet/order_information'),
        hidden: true
      },
      {
        path: 'order_reconciliation',
        name: 'OrderReconciliation',
        meta: {
          title: '订单对账',
          icon: '',
          identify: 'b2b:financial:order_reconciliation'
          
        },
        component: () => import('@/subject/pop/views_b2b/financial/order_reconciliation')
      },
      {
        path: 'order_reconciliation_detailed/:id',
        name: 'OrderReconciliationDetailed',
        meta: {
          title: '订单对账详情',
          icon: '',
          identify: 'b2b:financial:order_reconciliation_detailed',
          activeMenu: ['/financial/order_reconciliation'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/financial/order_reconciliation/order_reconciliation_detailed'),
        hidden: true
      }
    ]
  }
]
export default route
