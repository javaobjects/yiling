const route = [
  {
    path: '/rebate',
    alwaysShow: true,
    redirect: '/rebate/rebate_apply_finance',
    name: '',
    meta: { title: '企业返利', icon: 'icon13', identify: 'pop:rebate' },
    children: [
      {
        path: 'rebate_apply_finance',
        name: 'RebateApply',
        meta: {
          title: '入账申请财务',
          icon: '',
          identify: 'pop:rebate:rebate_apply_finance',
          type: 2
        },
        component: () => import('@/subject/pop/views/rebate/rebate_apply')
      },
      {
        path: 'enter_account_apply',
        name: 'EnterAccountApply',
        meta: {
          title: '申请入账',
          icon: '',
          identify: 'pop:rebate:enter_account_apply',
          activeMenu: ['/rebate/rebate_apply_business', '/rebate/rebate_apply_finance'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views/rebate/enter_account_apply'),
        hidden: true
      },
      {
        path: 'rebate_apply_info/:id/:isAudit/:isEdit',
        name: 'RebateApplyInfo',
        meta: {
          title: '申请入账返利信息',
          icon: '',
          identify: 'pop:rebate:rebate_apply_info',
          activeMenu: ['/rebate/rebate_apply_business', '/rebate/rebate_apply_finance', '/rebate/rebate_check'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/rebate/rebate_apply_info'),
        hidden: true
      },
      {
        path: 'rebate_use',
        name: 'RebateUse',
        meta: {
          title: '企业返利使用',
          icon: '',
          identify: 'pop:rebate:rebate_use'
        },
        component: () => import('@/subject/pop/views/rebate/rebate_use')
      },
      {
        path: 'rebate_check',
        name: 'RebateCheck',
        meta: {
          title: '企业返利对账',
          icon: '',
          identify: 'pop:rebate:rebate_check'
        },
        component: () => import('@/subject/pop/views/rebate/rebate_check')
      },
      {
        path: 'rebate_used_list/:id/:easCode/:usedAmount',
        name: 'RebateUsedList',
        meta: {
          title: '已使用返利列表',
          icon: '',
          identify: 'pop:rebate:rebate_used_list',
          activeMenu: ['/rebate/rebate_check'],
          noCache: true
        },
        component: () => import('@/subject/pop/views/rebate/rebate_check/rebate_used_list'),
        hidden: true
      }
    ]
  }
]

export default route
