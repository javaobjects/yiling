const route = [
  {
    path: '/cmp_drug_agent_manage',
    redirect: '/cmp_drug_agent_manage/drug_agent_list',
    name: '',
    alwaysShow: true,
    meta: { title: '药代管理cmp', icon: 'drug_agent', identify: 'cmp:drug_agent_manage' },
    children: [
      {
        path: 'drug_agent_list',
        name: 'DrugAgentList',
        meta: { title: '药代信息', icon: '', identify: 'cmp:drug_agent_manage:drug_agent_list' },
        component: () => import('@/subject/pop/views_cmp/drug_agent_manage/drug_agent_list/index')
      },
      {
        path: 'drug_agent_detail/:id',
        name: 'DrugAgentDetail',
        meta: {
          title: '药代详情',
          icon: '',
          identify: 'cmp:drug_agent_manage:drug_agent_detail',
          activeMenu: ['/sale_drug_agent_manage/drug_agent_list'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_cmp/drug_agent_manage/drug_agent_detail/index'),
        hidden: true
      }
    ]
  }
]

export default route
