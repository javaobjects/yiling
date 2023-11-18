const route = [
  {
    path: '/benefit_plan',
    redirect: '/benefit_plan/plan_index',
    name: 'BenefitPlan',
    meta: { title: '药品福利', icon: 'benefit_plan', identify: 'cmp:benefit_plan' },
    children: [
      {
        path: 'plan_index',
        name: 'PlanIndex',
        meta: {
          title: '药品福利计划',
          icon: '',
          identify: 'cmp:benefit_plan:plan_index'
        },
        component: () => import('@/subject/admin/views_cmp/benefit_plan/plan_index')
      },
      {
        path: 'plan_index_edit/:id',
        name: 'PlanIndexEdit',
        meta: {
          title: '编辑',
          icon: '',
          identify: 'cmp:benefit_plan:plan_index_edit',
          activeMenu: ['/benefit_plan/plan_index'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_cmp/benefit_plan/plan_index/edit'),
        hidden: true
      },
      {
        path: 'terminal_merchants',
        name: 'TerminalMerchants',
        meta: {
          title: '终端商家',
          icon: '',
          identify: 'cmp:benefit_plan:terminal_merchants'
        },
        component: () => import('@/subject/admin/views_cmp/benefit_plan/terminal_merchants')
      },
      {
        path: 'terminal_merchants_add',
        name: 'TerminalMerchantsAdd',
        meta: {
          title: '添加',
          icon: '',
          identify: 'cmp:benefit_plan:terminal_merchants_add',
          activeMenu: ['/benefit_plan/terminal_merchants'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_cmp/benefit_plan/terminal_merchants/establish'),
        hidden: true
      }
    ]
  }
]
export default route