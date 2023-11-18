const route = [
  // {
  //   path: "/company",
  //   alwaysShow: true,
  //   redirect: "/company/company_index",
  //   name: "",
  //   meta: { title: "企业账号", icon: "icon1", identify: "pop:enterprise" },
  //   children: [
  //     {
  //       path: "company_index",
  //       name: "CompanyIndex",
  //       meta: {
  //         title: "企业信息查询",
  //         icon: "",
  //         identify: "pop:enterprise:index"
  //       },
  //       component: () => import("@/subject/pop/views/company/index")
  //     }
  //   ]
  // },
  {
    path: '/flow_direction',
    alwaysShow: true,
    redirect: '/flow_direction/flow_direction_first',
    name: '',
    meta: { title: '流向管理', icon: 'icon11', identify: 'pop:flow_direction' },
    children: [
      {
        path: 'flow_direction_first',
        name: 'FlowDirectionFirst',
        meta: {
          title: '以岭发货数据',
          icon: '',
          identify: 'pop:flow_direction:first'
        },
        component: () =>
          import('@/subject/pop/views/flow_direction/flow_direction_first')
      },
      {
        path: 'flow_direction_second',
        name: 'FlowDirectionSecond',
        meta: { title: '流向(一级商销售)', icon: '', identify: 'pop:flow_direction:second' },
        component: () => import('@/subject/pop/views/flow_direction/flow_direction_second')
      }
    ]
  }
]

export default route
