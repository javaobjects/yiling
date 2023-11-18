const route = [
  {
    path: '/credentials_review',
    redirect: '/credentials_review/review_record',
    name: '',
    meta: { title: '证件审核', icon: 'icon25', identify: 'sale:credentials_review' },
    children: [
      {
        path: 'review_record',
        name: 'ReviewRecord',
        meta: { title: '证件审核列表', icon: '', identify: 'sale:credentials_review:review_record' },
        component: () => import('@/subject/admin/views_sale/credentials_review/review_record/index')
      },
      {
        path: 'review_record_detail/:id',
        name: 'ReviewRecordDetail',
        meta: { title: '审核详情', icon: '', identify: 'sale:credentials_review:review_record_detail', activeMenu: ['/credentials_review/review_record'], noCache: true },
        component: () => import('@/subject/admin/views_sale/credentials_review/review_record_detail/index'),
        hidden: true
      }

    ]
  }
]

export default route
