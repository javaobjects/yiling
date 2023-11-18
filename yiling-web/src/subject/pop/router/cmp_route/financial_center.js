// 财务中心
const route = [
  {
    path: '/financial_center',
    alwaysShow: true,
    redirect: '/financial_center/settlement_sheet',
    name: '',
    meta: { 
      title: '财务中心cmp',
      icon: 'icon23', 
      identify: 'cmp:financial_center' 
    },
    children: [
      {
        path: 'settlement_sheet',
        name: 'SettlementSheet',
        meta: {
          title: '药品结算账单',
          icon: '', 
          identify: 'cmp:financial_center:settlement_sheet'
        },
        component: () => import('@/subject/pop/views_cmp/financial_center/settlement_sheet/index')
      }
    ]
  }
];

export default route;
