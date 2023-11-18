const route = [
  {
    path: '/erp_flow',
    redirect: '/erp_flow/erp_bussiness_stock',
    alwaysShow: true,
    name: '',
    meta: { title: 'erp流向', icon: 'icon24', identify: 'pop:erp_flow' },
    children: [
      {
        path: 'erp_bussiness_stock',
        name: 'ErpBussinessStock',
        meta: {title: '企业库存',icon: '',identify: 'pop:erp_flow:erp_bussiness_stock' },
        component: () => import('@/subject/pop/views/erp_flow/business_stock')
      },
      {
        path: 'erp_bussiness_sale_flow',
        name: 'ErpBussinessSaleFlow',
        meta: { title: '企业销售流向', icon: '', identify: 'pop:erp_flow:erp_bussiness_sale_flow' },
        component: () => import('@/subject/pop/views/erp_flow/business_sale_flow')
      },
      {
        path: 'erp_bussiness_buy_flow',
        name: 'ErpBussinessBuyFlow',
        meta: { title: '企业采购流向', icon: '', identify: 'pop:erp_flow:erp_bussiness_buy_flow' },
        component: () => import('@/subject/pop/views/erp_flow/business_buy_flow')
      },
      {
        path: 'erp_chain_flow',
        name: 'ErpChainFlow',
        meta: { title: '连锁纯销流向', icon: '', identify: 'pop:erp_flow:erp_chain_flow' },
        component: () => import('@/subject/pop/views/erp_flow/erp_chain_flow')
      }
    ]
  }
];

export default route;
