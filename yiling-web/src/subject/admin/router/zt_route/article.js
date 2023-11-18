const route = [
  {
    path: '/article',
    redirect: '/article/article_list',
    name: '',
    meta: { title: '文章管理', icon: 'icon24', identify: 'zt:article' },
    children: [
      {
        path: 'article_list',
        name: 'ArticleList',
        meta: {
          title: '文章列表',
          icon: '',
          identify: 'zt:article:article_list'
        },
        component: () => import('@/subject/admin/views_zt/article/article_list')
      },
      {
        path: 'article_list_detail/:id',
        name: 'ArticleListDetail',
        meta: {
          title: '新建/编辑',
          icon: '',
          identify: 'zt:article_list:article_list_detail',
          activeMenu: ['*'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/article/article_list_detail'),
        hidden: true
      }
    ]
  }
]

export default route
