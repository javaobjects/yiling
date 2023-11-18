const route = [
  {
    path: '/statistical_report',
    redirect: '/statistical_report/statistical_data',
    name: '',
    meta: { title: '统计报表', icon: 'statistical_report', identify: 'zt:statistical_report' },
    children: [
      {
        path: 'statistical_data',
        name: 'StatisticalData',
        meta: {
          title: '数据信息',
          icon: '',
          identify: 'zt:statistical_report:statistical_data'
        },
        component: () => import('@/subject/admin/views_zt/statistical_report/statistical_data')
      },
      {
        path: 'b2b_statistical',
        name: 'B2BStatistical',
        meta: {
          title: 'B2B订单报表',
          icon: '',
          identify: 'zt:statistical_report:b2b_statistical'
        },
        component: () => import('@/subject/admin/views_zt/statistical_report/b2b_statistical')
      }
    ]
  }
]

export default route
