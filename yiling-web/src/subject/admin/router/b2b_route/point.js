const route = [
  {
    path: '/point',
    redirect: '/point/point_list',
    alwaysShow: true,
    name: '',
    meta: { title: '积分管理', icon: 'icon21', identify: 'b2b:point' },
    hidden: true,
    children: [
      {
        path: 'point_list',
        name: 'PointList',
        meta: { title: '积分查看', icon: '', identify: 'b2b:point:list'},
        component: () => import('@/subject/admin/views_b2b/point/index')
      }
    ]
  }
]

export default route
