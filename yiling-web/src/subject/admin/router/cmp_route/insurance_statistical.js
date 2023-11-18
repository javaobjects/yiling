const route = [
  {
    path: '/insurance_statistical',
    redirect: '/insurance_statistical/qrcode_statistical',
    name: '',
    meta: { title: '保险统计', icon: 'insurance_statistical', identify: 'cmp:insure_statistical' },
    children: [
      {
        path: 'qrcode_statistical',
        name: 'QrcodeStatistical',
        meta: { title: '药盒二维码单页统计', icon: '', identify: 'cmp:insure_statistical:qrcode_statistical' },
        component: () => import('@/subject/admin/views_cmp/insurance_statistical/qrcode_statistical/index')
      },
      {
        path: 'welfare_statistics',
        name: 'WelfareStatistics',
        meta: { 
          title: '用药福利统计', 
          icon: '', 
          identify: 'cmp:insure_statistical:welfare_statistics'
        },
        component: () => import('@/subject/admin/views_cmp/insurance_statistical/welfare_statistics')
      }
    ]
  }
]

export default route