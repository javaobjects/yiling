const route = [
    {
      path: '/commission_admin',
      redirect: '/commission_admin/commission_settlement',
      name: '',
      meta: { title: '佣金管理', icon: 'icon13', identify: 'sale:sale_commission' },
      children: [
        {
          path: 'commission_settlement',
          name: 'CommissionSettlement',
          meta: { title: '佣金结算', icon: '', identify: 'sale:sale_commission:sale_commission_settlement' },
          component: () => import('@/subject/admin/views_sale/commission_admin/commission_settlement/index')
        },
        {
          path: 'commission_settlement_detail/:id',
          name: 'CommissionSettlementDetail',
          meta: { title: '查看明细', icon: '', identify: 'sale:sale_commission:sale_commission_settlement_detail', activeMenu: ['*'] },
          component: () => import('@/subject/admin/views_sale/commission_admin/commission_settlement_detail/index.vue'),
          hidden: true
        }
        
      ]
    }
  ]
  
  export default route
  