const route = [
  {
    path: '/evaluation',
    redirect: '/evaluation/gauge',
    name: 'Evaluation',
    meta: { title: '测评管理', icon: 'evaluation', identify: 'content:evaluation' },
    children: [
      {
        path: 'gauge',
        name: 'Gauge',
        meta: {
          title: '量表管理',
          icon: '',
          identify: 'content:evaluation:gauge'
        },
        component: () => import('@/subject/admin/views_content/evaluation/gauge')
      },
      {
        path: 'add_gauge/:id',
        name: 'AddGauge',
        meta: {
          title: '新建/设置量表',
          icon: '',
          identify: 'content:evaluation:add_gauge',
          activeMenu: ['/evaluation/gauge'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_content/evaluation/gauge/addGauge'),
        hidden: true
      },
      {
        path: 'result_settings/:title?/:id',
        name: 'ResultSettings',
        meta: {
          title: '结果设置',
          icon: '',
          identify: 'content:evaluation:result_settings',
          activeMenu: ['/evaluation/gauge'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_content/evaluation/gauge/resultSettings'),
        hidden: true
      },
      {
        path: 'topic_setting/:id',
        name: 'TopicSetting',
        meta: {
          title: '题目设置',
          icon: '',
          identify: 'content:evaluation:topic_setting',
          activeMenu: ['/evaluation/gauge'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_content/evaluation/gauge/topicSetting'),
        hidden: true
      },
      {
        path: 'marketing_settings/:title?/:id',
        name: 'MarketingSettings',
        meta: {
          title: '营销设置',
          icon: '',
          identify: 'content:evaluation:marketing_settings',
          activeMenu: ['/evaluation/gauge'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_content/evaluation/gauge/marketingSettings'),
        hidden: true
      }
    ]
  }
]
export default route