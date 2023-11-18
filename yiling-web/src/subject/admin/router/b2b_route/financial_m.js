const route = [
  {
    path: '/financial',
    alwaysShow: true,
    name: '',
    redirect: '/financial/settlement_sheet',
    meta: { title: '财务管理', icon: 'icon23', identify: 'b2b:financial' },
    children: [
      {
        path: 'settlement_sheet',
        name: 'SettlementSheet',
        meta: {
          title: '结算单对账',
          icon: '',
          identify: 'b2b:financial:settlement_sheet'
        },
        component: () => import('@/subject/admin/views_b2b/financial/settlement_sheet')
      },
      {
        path: 'settlement_sheet_detailed/:id',
        name: 'SettlementSheetDetailed',
        meta: {
          title: '结算单明细',
          icon: '',
          identify: 'b2b:financial:settlement_sheet_detailed',
          activeMenu: ['/financial/settlement_sheet'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/financial/settlement_sheet/settlement_sheet_detailed'),
        hidden: true
      },
      {
        path: 'order_item_detailed/:orderId?/:settlementId',
        name: 'OrderItemDetailed',
        meta: {
          title: '订单商品明细',
          icon: '',
          identify: 'b2b:financial:order_item_detailed',
          activeMenu: ['/financial/settlement_sheet'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/financial/settlement_sheet/order_item_detailed'),
        hidden: true
      },
      {
        path: 'order_reconciliation',
        name: 'OrderReconciliation',
        meta: {
          title: '订单对账',
          icon: '',
          identify: 'pop:financial:order_reconciliation'
        },
        component: () => import('@/subject/admin/views_b2b/financial/order_reconciliation')
      },
      {
        path: 'order_reconciliation_details/:orderId',
        name: 'OrderReconciliationDetails',
        meta: {
          title: '订单明细',
          icon: '',
          identify: 'pop:financial:order_reconciliation_details',
          activeMenu: ['/financial/order_reconciliation'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/financial/order_reconciliation/details'),
        hidden: true
      },
      {
        path: 'refund_slip',
        name: 'RefundSlip',
        meta: {
          title: '退款单',
          icon: '',
          identify: 'pop:financial:refund_slip'
        },
        component: () => import('@/subject/admin/views_b2b/financial/refund_slip')
      },
      {
        path: 'payment',
        name: 'Payment',
        meta: {
          title: '重复支付',
          icon: '',
          identify: 'pop:financial:payment'
         
        },
        component: () => import('@/subject/admin/views_b2b/financial/payment')
      },
      {
        path: 'member_return_record',
        name: 'MemberReturnRecord',
        meta: {
          title: '会员回款记录',
          icon: '',
          identify: 'b2b:financial:member_return_record'
         
        },
        component: () => import('@/subject/admin/views_b2b/financial/member_return_record')
      }
    ]
  }
]
export default route