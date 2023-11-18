const route = [
  {
    path: '/erpAdministration',
    redirect: '/erpAdministration/erp_list',
    name: '',
    meta: { title: 'ERP管理', icon: 'erp', identify: 'zt:erpAdministration' },
    children: [
      {
        path: 'erp_list',
        name: 'ErpList',
        meta: {
          title: '客户异常管理',
          icon: '',
          identify: 'zt:erpAdministration:erp_list'
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/erp_list')
      },
      {
        path: 'erp_maintain/:id',
        name: 'ErpMaintain',
        meta: {
          title: '维护',
          icon: '',
          identify: 'zt:erpAdministration:erp_maintain',
          activeMenu: ['/erpAdministration/erp_list'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/erp_maintain'),
        hidden: true
      },
      {
        path: 'erp_matching/:id',
        name: 'ErpMatching',
        meta: {
          title: '终端匹配',
          icon: '',
          identify: 'zt:erpAdministration:erp_matching',
          activeMenu: ['/erpAdministration/erp_list'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/erp_matching'),
        hidden: true
      },
      {
        path: 'enterprise',
        name: 'Enterprise',
        meta: {
          title: '对接企业管理',
          icon: '',
          identify: 'zt:erpAdministration:enterprise'
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/enterprise')
      },
      {
        path: 'enterprise_detail/:id',
        name: 'EnterpriseDetail',
        meta: {
          title: '编辑对接客户',
          icon: '',
          identify: 'zt:erpAdministration:enterprise_detail',
          activeMenu: ['/erpAdministration/enterprise'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/enterprise_detail'),
        hidden: true
      },
      {
        path: 'enterprise_added',
        name: 'EnterpriseAdded',
        meta: {
          title: '新增对接客户',
          icon: '',
          identify: 'zt:erpAdministration:enterprise_added',
          activeMenu: ['/erpAdministration/enterprise'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/enterprise_added'),
        hidden: true
      },
      {
        path: 'erp_monitor',
        name: 'ErpMonitor',
        meta: {
          title: 'ERP监控',
          icon: '',
          identify: 'zt:erpAdministration:erp_monitor'
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/erp_monitor')
      },
      {
        path: 'erp_monitor_details/:id',
        name: 'ErpMonitorDetails',
        meta: {
          title: 'ERP操作',
          icon: '',
          identify: 'zt:erpAdministration:erp_monitor_details',
          activeMenu: ['/erpAdministration/erp_monitor'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/erp_monitor/details'),
        hidden: true
      },
      {
        path: 'chain_relationship',
        name: 'ChainRelationship',
        meta: {
          title: '连锁总店对应关系',
          icon: '',
          identify: 'zt:erpAdministration:chain_relationship'
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/chain_relationship')
      }
    ]
  },
  {
    path: '/erpFlowDirection',
    redirect: '/erpFlowDirection/flowdirection_hold',
    name: '',
    meta: { title: 'ERP流向', icon: 'erp', identify: 'zt:erpFlowDirection' },
    children: [
      {
        path: 'flowdirection_hold',
        name: 'FlowdirectionHold',
        meta: {
          title: '流向封存',
          icon: '',
          identify: 'zt:erpFlowDirection:flowdirection_hold'
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/flowdirection_hold')
      },
      {
        path: 'flowdirection_hold_add',
        name: 'FlowdirectionHoldAdd',
        meta: {
          title: '添加流向封存',
          icon: '',
          identify: 'zt:erpFlowDirection:flowdirection_hold_add',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/flowdirection_hold_add'),
        hidden: true
      },
      {
        path: 'enterprise_goods_config',
        name: 'EnterpriseGoodsConfig',
        meta: {
          title: '企业流向商品配置',
          icon: '',
          identify: 'zt:erpFlowDirection:enterprise_goods_config'
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/enterprise_goods_config')
      },
      {
        path: 'month_flowto_monitor',
        name: 'MonthFlowtoMonitor',
        meta: {
          title: '月流向监控',
          icon: '',
          identify: 'zt:erpFlowDirection:month_flowto_monitor'
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/month_flowto_monitor')
      },
      {
        path: 'commodity_details/:eid?/:monthTime',
        name: 'CommodityDetails',
        meta: {
          title: '商品统计详情',
          icon: '',
          identify: 'zt:erpFlowDirection:commodity_details',
          activeMenu: ['/erpFlowDirection/month_flowto_monitor'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/month_flowto_monitor/details'),
        hidden: true
      }
    ]
  },
  {
    path: '/erpFlowDirectionInquire',
    redirect: '/erpFlowDirectionInquire/erp_bussiness_stock',
    name: '',
    meta: { title: '流向查询', icon: 'erp', identify: 'zt:erpFlowDirectionInquire' },
    children: [
      {
        path: 'erp_bussiness_stock',
        name: 'ErpBussinessStock',
        meta: {title: '企业库存',icon: '',identify: 'zt:erpFlowDirectionInquire:erp_bussiness_stock' },
        component: () => import('@/subject/admin/views_zt/erpAdministration/business_stock')
      },
      {
        path: 'erp_bussiness_stocks/:name',
        name: 'ErpBussinessStocks',
        meta: {
          title: '企业库存',
          icon: '',
          identify: 'zt:erpFlowDirectionInquire:erp_bussiness_stocks',
          activeMenu: ['/erpFlowDirectionInquire/erp_bussiness_stock'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/business_stock'),
        hidden: true
      },
      {
        path: 'erp_bussiness_sale_flow',
        name: 'ErpBussinessSaleFlow',
        meta: { title: '企业销售流向', icon: '', identify: 'zt:erpFlowDirectionInquire:erp_sale_flow' },
        component: () => import('@/subject/admin/views_zt/erpAdministration/business_sale_flow')
      },
      {
        path: 'erp_bussiness_sale_flows/:name?/:time',
        name: 'ErpBussinessSaleFlows',
        meta: { 
          title: '企业销售流向', 
          icon: '', 
          identify: 'zt:erpFlowDirectionInquire:erp_bussiness_sale_flows',
          activeMenu: ['/erpFlowDirectionInquire/erp_bussiness_sale_flow'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/business_sale_flow'),
        hidden: true
      },
      {
        path: 'erp_bussiness_buy_flow',
        name: 'ErpBussinessBuyFlow',
        meta: { title: '企业采购流向', icon: '', identify: 'zt:erpFlowDirectionInquire:erp_bussiness_buy_flow' },
        component: () => import('@/subject/admin/views_zt/erpAdministration/business_buy_flow')
      },
      {
        path: 'erp_bussiness_buy_flows/:name?/:time',
        name: 'ErpBussinessBuyFlows',
        meta: { 
          title: '企业采购流向', 
          icon: '', 
          identify: 'zt:erpFlowDirectionInquire:erp_bussiness_buy_flows',
          activeMenu: ['/erpFlowDirectionInquire/erp_bussiness_buy_flow'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/erpAdministration/business_buy_flow'),
        hidden: true
      },
      {
        path: 'erp_chain_flow',
        name: 'ErpChainFlow',
        meta: { title: '连锁纯销流向', icon: '', identify: 'zt:erpFlowDirectionInquire:erp_chain_flow' },
        component: () => import('@/subject/admin/views_zt/erpAdministration/erp_chain_flow')
      }
    ]
  }
]

export default route
