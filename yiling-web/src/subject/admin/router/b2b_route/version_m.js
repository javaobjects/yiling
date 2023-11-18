const route = [
  {
    path: '/version',
    alwaysShow: true,
    name: '',
    redirect: '/version/version_manage',
    meta: { title: '版本管理', icon: 'icon13', identify: 'b2b:version' },
    children: [
      {
        path: 'version_manage',
        name: 'VersionManage',
        meta: {
          title: '版本设置',
          icon: '',
          identify: 'b2b:version:version_manage'
        },
        component: () => import('@/subject/admin/views_b2b/version')
      }
    ]
  }
]
export default route