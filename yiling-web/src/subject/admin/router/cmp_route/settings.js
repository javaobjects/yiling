const route = [
  {
    path: '/basic_settings',
    redirect: '/basic_settings/advertising_manage',
    name: 'BasicSettings',
    meta: { title: '2C基础设置', icon: 'cmp_settings', identify: 'cmp:basic_settings' },
    children: [
      {
        path: 'advertising_manage',
        name: 'AdvertisingManage',
        meta: {
          title: '广告管理',
          icon: '',
          identify: 'cmp:basic_settings:advertising_manage'
        },
        component: () => import('@/subject/admin/views_cmp/basic_settings/advertising_manage')
      },
      {
        path: 'advertising_manage_details/:id',
        name: 'AdvertisingManageDetails',
        meta: {
          title: '编辑/创建',
          icon: '',
          identify: 'cmp:basic_settings:advertising_manage_details',
          activeMenu: ['/basic_settings/advertising_manage'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/basic_settings/advertising_manage/details')
      },
      {
        path: 'version_update',
        name: 'VersionUpdate',
        meta: {
          title: 'APP版本更新',
          icon: '',
          identify: 'cmp:basic_settings:version_update'
        },
        component: () => import('@/subject/admin/views_cmp/basic_settings/version_update')
      }
    ]
  }
]
export default route