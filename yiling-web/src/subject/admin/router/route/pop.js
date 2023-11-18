const route = [
  {
    path: '/pop',
    redirect: '/pop/pop_nav',
    name: '',
    meta: { title: 'POP管理', icon: 'icon5', identify: 'sys:pop' },
    children: [
      {
        path: 'pop_nav',
        name: 'PopNav',
        meta: { title: '导航管理', icon: '', identify: 'sys:pop:nav' },
        component: () => import('@/subject/admin/views/pop/nav')
      },
      {
        path: 'pop_recommend',
        name: 'PopRecommend',
        meta: {
          title: '首页推荐位管理',
          icon: '',
          identify: 'sys:pop:recommend'
        },
        component: () => import('@/subject/admin/views/pop/recommend')
      },
      {
        path: 'pop_banner',
        name: 'PopBanner',
        meta: { title: 'Banner管理', icon: '', identify: 'sys:pop:banner' },
        component: () => import('@/subject/admin/views/pop/banner')
      },
      {
        path: 'pop_banner_edit/:id?',
        name: 'PopBannerEdit',
        meta: { title: 'Banner管理详情', icon: '', identify: 'sys:pop:banner_edit', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/admin/views/pop/banner/banner_edit'),
        hidden: true
      },
      {
        path: 'pop_hot_word',
        name: 'PopHotWord',
        meta: { title: '热词管理', icon: '', identify: 'sys:pop:hot_word' },
        component: () => import('@/subject/admin/views/pop/hot_word')
      },
      {
        path: 'hot_word_edit/:id?',
        name: 'HotWordEdit',
        meta: { title: '热词管理详情', icon: '', identify: 'sys:pop:hot_word_edit', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/admin/views/pop/hot_word/hot_word_edit'),
        hidden: true
      },
      {
        path: 'goods_category',
        name: 'GoodsCategory',
        meta: { title: '分类商品管理', icon: '', identify: 'sys:pop:goods_category' },
        component: () => import('@/subject/admin/views/pop/goods_category')
      },
      {
        path: 'goods_category_edit/:id?',
        name: 'GoodsCategoryEdit',
        meta: { title: '分类商品详情', icon: '', identify: 'sys:pop:goods_category_edit', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/admin/views/pop/goods_category/goods_category_edit'),
        hidden: true
      },
      {
        path: 'pop_notice',
        name: 'PopNotice',
        meta: { title: '公告管理', icon: '', identify: 'sys:pop:notice' },
        component: () => import('@/subject/admin/views/pop/notice')
      },
      {
        path: 'pop_notice_edit/:id',
        name: 'PopNoticeEdit',
        meta: { title: '编辑公告', icon: '', identify: 'sys:pop:notice_edit', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/admin/views/pop/notice_editor'),
        hidden: true
      }
    ]
  }
]

export default route
