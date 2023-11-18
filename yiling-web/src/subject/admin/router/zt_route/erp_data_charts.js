const route = [
  {
    path: '/zt_erpDataCharts',
    redirect: '/zt_erpDataCharts/erpData',
    name: '',
    meta: { title: 'ERP流向数据', icon: 'icon31', identify: 'zt:zt_erpDataCharts' },
    children: [
      {
        path: 'erpData',
        name: 'ErpDataChart',
        meta: {
          title: 'ERP流向数据分析',
          icon: '',
          identify: 'zt:zt_erpDataCharts:erpData',
          activeMenu: ['/zt_erpDataCharts/erpData']
        },
        component: () => import('@/subject/admin/views_zt/erp_data_charts/erp_data/index')
      },
      {
        path: 'erp_purchase_storage_chart',
        name: 'ErpPurchaseStorageChart',
        meta: {
          title: 'ERP采购入库数据集',
          icon: '',
          identify: 'zt:zt_erpDataCharts:erp_purchase_storage_chart',
          activeMenu: ['/zt_erpDataCharts/erp_purchase_storage_chart']
        },
        component: () => import('@/subject/admin/views_zt/erp_data_charts/erp_purchase_storage_chart/index')
      }
    ]
  }
]

export default route
