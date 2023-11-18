const route = [
  {
    path: '/zt_company',
    alwaysShow: true,
    name: '',
    redirect: '/zt_company/zt_company_index',
    meta: { title: '企业账号', icon: 'icon1', identify: 'zt:zt_company' },
    children: [
      {
        path: 'zt_company_index',
        name: 'ZtCompanyIndex',
        meta: {
          title: '企业信息查询',
          icon: '',
          identify: 'zt:zt_company:zt_company_index'
        },
        component: () => import('@/subject/admin/views_zt/company/index')
      },
      {
        path: 'zt_company_detail/:id',
        name: 'ZtCompanyDetail',
        meta: {
          title: '企业详情',
          icon: '',
          identify: 'zt:zt_company:zt_company_detail',
          activeMenu: ['/zt_company/zt_company_index'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/company/detail/index'),
        hidden: true
      },
      {
        path: 'zt_company_user',
        name: 'ZtCompanyUser',
        meta: {
          title: '账号信息查询',
          icon: '',
          identify: 'zt:zt_company:zt_company_user'
        },
        component: () => import('@/subject/admin/views_zt/company/user/index')
      },
      {
        path: 'zt_company_user_detail/:id',
        name: 'ZtCompanyUserDetail',
        meta: {
          title: '账号详情',
          icon: '',
          identify: 'zt:zt_company:zt_company_user_detail',
          activeMenu: ['/zt_company/zt_company_user'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/company/userDetail/index'),
        hidden: true
      },
      {
        path: 'zt_label',
        name: 'ZtLabel',
        meta: {
          title: '标签管理',
          icon: '',
          identify: 'zt:zt_company:zt_label'
        },
        component: () => import('@/subject/admin/views_zt/company/zt_label/index')
      },
      {
        path: 'zt_maintain_Tabel/:row',
        name: 'ZtMaintainLabel',
        meta: {
          title: '新建标签',
          icon: '',
          identify: 'zt:zt_company:zt_maintain_Tabel',
          activeMenu: ['/zt_company/zt_label'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/company/zt_label/zt_maintain_Tabel/index'),
        hidden: true
      },
      {
        path: 'download_promotion',
        name: 'DownloadPromotion',
        meta: {
          title: '下载推广查看',
          icon: '',
          identify: 'zt:zt_company:download_promotion'
        },
        component: () => import('@/subject/admin/views_zt/company/download_promotion/index')
      }
    ]
  }
]

export default route 