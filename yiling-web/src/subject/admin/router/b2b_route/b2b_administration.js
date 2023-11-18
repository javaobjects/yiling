const route = [
  {
    path: '/b2bAdministration',
    alwaysShow: true,
    name: '',
    redirect: '/b2bAdministration/b2b_home',
    meta: { title: 'b2b管理', icon: 'icon18', identify: 'b2b:b2bAdministration' },
    children: [
      {
        path: 'b2b_home',
        name: 'B2BHome',
        meta: {
          title: '金刚位',
          icon: '',
          identify: 'b2b:b2bAdministration:b2b_home'
        },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/b2b_home')
      },
      {
        path: 'b2b_home_establish/:modifyData',
        name: 'B2BHomeEstablish',
        meta: {
          title: '创建/修改金刚位',
          icon: '',
          identify: 'b2b:b2bAdministration:b2b_home_establish',
          activeMenu: ['/b2bAdministration/b2b_home'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/b2b_home/establish'),
        hidden: true
      },     
      {
        path: 'banner',
        name: 'Banner',
        meta: {
          title: 'banner',
          icon: '',
          identify: 'b2b:b2bAdministration:banner'
        },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/banner')
      },
      {
        path: 'banner_establis/:id',
        name: 'BannerEstablis',
        meta: {
          title: '新增Banner',
          icon: '',
          identify: 'b2b:b2bAdministration:banner_establis',
          activeMenu: ['/b2bAdministration/banner'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/banner/establish'),
        hidden: true
      },
      {
        path: 'b2b_hot_word',
        name: 'B2bHotWord',
        meta: { title: '热词管理', icon: '', identify: 'b2b:b2bAdministration:b2b_hot_word' },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/hot_word')
      },
      {
        path: 'b2b_hot_word_edit/:id?',
        name: 'B2bHotWordEdit',
        meta: {
          title: '热词管理详情', 
          icon: '', 
          identify: 'b2b:b2bAdministration:hot_word_edit', 
          activeMenu: ['/b2bAdministration/b2b_hot_word'],
          noCache: true 
        },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/hot_word/hot_word_edit'),
        hidden: true
      },
      {
        path: 'home_page',
        name: 'HomePage',
        meta: {
          title: '开屏位',
          icon: '',
          identify: 'b2b:b2bAdministration:home_page'
        },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/home_page')
      },
      {
        path: 'home_page_edit/:type?/:id',
        name: 'HomePageEdit',
        meta: {
          title: '新增/编辑', 
          icon: '', 
          identify: 'b2b:b2bAdministration:home_page_edit', 
          activeMenu: ['/b2bAdministration/home_page'],
          noCache: true 
        },
        component: () => import('@/subject/admin/views_b2b/b2bAdministration/home_page/establish'),
        hidden: true
      }
    ]
  }
]
export default route