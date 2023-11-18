const route = [
  {
    path: '/app_maintain',
    redirect: '/app_maintain/app_administration',
    name: 'AppMaintain',
    meta: { title: 'APP维护', icon: 'icon1', identify: 'sale:app_maintain' },
    children: [
      {
        path: 'app_administration',
        name: 'AppAdministration',
        meta: {
          title: 'banner',
          icon: '',
          identify: 'sale:app_maintain:app_administration'
        },
        component: () => import('@/subject/admin/views_sale/app_maintain/app_administration')
      },
      {
        path: 'app_administration_details/:id',
        name: 'AppAdministrationDetails',
        meta: {
          title: '添加banner',
          icon: '',
          identify: 'sale:app_maintain:app_administration_details',
          activeMenu: ['/app_maintain/app_administration'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_sale/app_maintain/app_administration/app_administration_details')
      },
      {
        path: 'edition_administration',
        name: 'EditionAdministration',
        meta: {
          title: '版本发布',
          icon: '',
          identify: 'sale:app_maintain:edition_administration'
        },
        component: () => import('@/subject/admin/views_sale/app_maintain/edition_administration')
      },
      {
        path: 'home_page',
        name: 'HomePage',
        meta: {
          title: '开屏位',
          icon: '',
          identify: 'sale:app_maintain:home_page'
        },
        component: () => import('@/subject/admin/views_sale/app_maintain/home_page')
      },
      {
        path: 'home_page_edit/:type?/:id',
        name: 'HomePageEdit',
        meta: {
          title: '新增/编辑', 
          icon: '', 
          identify: 'sale:app_maintain:home_page_edit', 
          activeMenu: ['/app_maintain/home_page'],
          noCache: true 
        },
        component: () => import('@/subject/admin/views_sale/app_maintain/home_page/establish'),
        hidden: true
      }

      // {
      //   path: "edition_administration_details",
      //   name: "EditionAdministrationDetails",
      //   meta: {
      //     title: "创建版本",
      //     icon: "",
      //     identify: "sale:app_maintain:edition_administration_details",
      //     activeMenu: ['/app_maintain/edition_administration'],
      //     noCache: true
      //   },
      //   component: () => import("@/subject/admin/views_sale/app_maintain/edition_administration/edition_administration_details"),
      //   hidden: true,
      // },
    ]
  }
]

export default route
