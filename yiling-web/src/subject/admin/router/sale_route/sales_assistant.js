const route = [
  {
    path: '/sales_assistant',
    name: 'SalesAssistant',
    redirect: '/sales_assistant/task_administration',
    meta: { title: '任务管理', icon: 'task', identify: 'sale:sales_assistant' },
    children: [
      {
        path: 'task_administration',
        name: 'TaskAdministration',
        meta: {
          title: '任务列表',
          icon: '',
          identify: 'sale:sales_assistant:task_administration'
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/task_administration/index')
      },
      {
        path: 'establishs/:type?/:id?',
        name: 'Establishs',
        meta: {
          title: '新建/编辑/修改任务',
          icon: '',
          identify: 'sale:sales_assistant:establishs',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/establishs/index'),
        hidden: true
      },
      // 销售量
      {
        path: 'task_racking/:id',
        name: 'TaskRacking',
        meta: {
          title: '任务追踪',
          icon: '',
          identify: 'sale:sales_assistant:task_racking',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/task_racking/index'),
        hidden: true
      },
      // 销售量
      {
        path: 'pull_people/:userTaskId?/:id?',
        name: 'PullPeople',
        meta: {
          title: '任务追踪详情',
          icon: '',
          identify: 'sale:sales_assistant:pull_people',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/pull_people/index'),
        hidden: true
      },
      // 拉户拉人
      {
        path: 'lahu_lapeople/:id',
        name: 'LahuLapeople',
        meta: {
          title: '任务追踪',
          icon: '',
          identify: 'sale:sales_assistant:lahu_lapeople',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/lahu_lapeople/index'),
        hidden: true
      },
      // 拉户拉人
      {
        path: 'lahularen_details/:userTaskId?/:id?/:finishType?',
        name: 'LahularenDetails',
        meta: {
          title: '任务追踪详情',
          icon: '',
          identify: 'sale:sales_assistant:lahularen_details',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/lahularen_details/index'),
        hidden: true
      },
      // 会员
      {
        path: 'member_information/:id',
        name: 'MemberInformation',
        meta: {
          title: '任务追踪',
          icon: '',
          identify: 'sale:sales_assistant:member_information',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/member_information/index'),
        hidden: true
      },
       // 会员 详情
      {
        path: 'member_details/:userTaskId?/:id?',
        name: 'MemberDetails',
        meta: {
          title: '任务追踪详情',
          icon: '',
          identify: 'sale:sales_assistant:member_details',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/member_details/index'),
        hidden: true
      },
      //上传资料 任务追踪详情
      {
        path: 'department_details/:userTaskId',
        name: 'DepartmentDetails',
        meta: {
          title: '任务追踪详情',
          icon: '',
          identify: 'sale:sales_assistant:department_details',
          activeMenu: ['/sales_assistant/task_administration'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_sale/sales_assistant/department_details/index'),
        hidden: true
      }
    ]
  }
]

export default route