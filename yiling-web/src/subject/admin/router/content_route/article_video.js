const route = [
  {
    path: '/article_video',
    redirect: '/article_video/article_list',
    name: 'ArticleVideo',
    meta: { title: '内容管理', icon: 'article_video', identify: 'content:article_video' },
    children: [
      {
        path: 'article_list',
        name: 'ArticleList',
        meta: {
          title: '文章列表',
          icon: '',
          identify: 'content:article_video:article_list'
        },
        component: () => import('@/subject/admin/views_content/article_video/article_list')
      },
      {
        path: 'article_drafts',
        name: 'ArticleDrafts',
        meta: {
          title: '草稿箱',
          icon: '',
          identify: 'content:article_video:article_drafts',
          activeMenu: ['/article_video/article_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/article_video/article_list/drafts')
      },
      {
        path: 'video_list',
        name: 'VideoList',
        meta: {
          title: '视频列表',
          icon: '',
          identify: 'content:article_video:video_list'
        },
        component: () => import('@/subject/admin/views_content/article_video/video_list')
      },
      {
        path: 'add_edit_article/:id?/:type?/:category?',
        name: 'AddEditArticle',
        meta: {
          title: '添加/编辑文章',
          icon: '',
          identify: 'content:article_video:add_edit_article',
          activeMenu: ['/article_video/article_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/article_video/article_list/add_edit_article')
      },
      {
        path: 'add_edit_video/:id?/:type?/:category?',
        name: 'AddEditVideo',
        meta: {
          title: '添加/编辑视频',
          icon: '',
          identify: 'content:article_video:add_edit_video',
          activeMenu: ['/article_video/video_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/article_video/video_list/add_edit_video')
      },
      {
        path: 'classification_setting',
        name: 'ClassificationSetting',
        meta: {
          title: '前台分类设置',
          icon: '',
          identify: 'content:article_video:classification_setting'
        },
        component: () => import('@/subject/admin/views_content/article_video/classification_setting')
      },
      {
        path: 'health_management',
        name: 'HealthManagement',
        meta: {
          title: '健康管理中心',
          icon: '',
          identify: 'content:article_video:health_management'
        },
        component: () => import('@/subject/admin/views_content/article_video/health_management')
      },
      {
        path: 'doctor_management',
        name: 'DoctorManagement',
        meta: {
          title: '医生端',
          icon: '',
          identify: 'content:article_video:doctor_management'
        },
        component: () => import('@/subject/admin/views_content/article_video/doctor_management')
      },
      {
        path: 'add_article',
        name: 'AddArticle',
        meta: {
          title: '添加引用',
          icon: '',
          identify: 'content:article_video:add_article',
          activeMenu: ['/article_video/health_management'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/article_video/health_management/details')
      },
      {
        path: 'add_doctor',
        name: 'AddDoctor',
        meta: {
          title: '添加引用',
          icon: '',
          identify: 'content:article_video:add_doctor',
          activeMenu: ['/article_video/doctor_management'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/article_video/doctor_management/details')
      },
      {
        path: 'view_statistics',
        name: 'ViewStatistics',
        meta: {
          title: '文章浏览量统计',
          icon: '',
          identify: 'content:article_video:view_statistics'
        },
        component: () => import('@/subject/admin/views_content/article_video/view_statistics')
      },
      {
        path: 'questions_and_answers',
        name: 'QuestionsAndAnswers',
        meta: {
          title: '内容问答管理',
          icon: '',
          identify: 'content:article_video:questions_and_answers'
        },
        component: () => import('@/subject/admin/views_content/article_video/questions_and_answers')
      },
      {
        path: 'sales_assistant',
        name: 'SalesAssistant',
        meta: {
          title: '销售助手',
          icon: '',
          identify: 'content:article_video:sales_assistant'
        },
        component: () => import('@/subject/admin/views_content/article_video/sales_assistant')
      },
      {
        path: 'add_sales',
        name: 'AddSales',
        meta: {
          title: '添加引用',
          icon: '',
          identify: 'content:article_video:add_sales',
          activeMenu: ['/article_video/sales_assistant'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/article_video/sales_assistant/details')
      },
      {
        path: 'big_canal',
        name: 'BigCanal',
        meta: {
          title: '大运河',
          icon: '',
          identify: 'content:article_video:big_canal'
        },
        component: () => import('@/subject/admin/views_content/article_video/big_canal')
      },
      {
        path: 'add_canal',
        name: 'AddCanal',
        meta: {
          title: '添加引用',
          icon: '',
          identify: 'content:article_video:add_canal',
          activeMenu: ['/article_video/big_canal'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/article_video/big_canal/details')
      }
    ]
  }
]
export default route