const route = [
  {
    path: '/protocol',
    alwaysShow: true,
    redirect: '/protocol/protocol_rebate',
    name: '',
    meta: { title: '原始协议查询', icon: 'icon5', identify: 'pop:protocol' },
    children: [
      {
        path: 'protocol_rebate',
        name: 'ProtocolRebate',
        meta: { title: '协议返利', icon: '', identify: 'pop:protocol:rebate' },
        component: () => import('@/subject/pop/views/protocol/protocol_rebate')
      },
      {
        path: 'protocol_list/:customerEid',
        name: 'ProtocolList',
        meta: { title: '协议返利', icon: '', identify: 'pop:protocol:list', activeMenu: ['/protocol/protocol_rebate'], noCache: true },
        component: () =>
          import('@/subject/pop/views/protocol/protocol_rebate/protocol_list'),
        hidden: true
      },
      {
        path: 'protocol_detail/:customerEid/:id',
        name: 'ProtocolDetail',
        meta: { title: '协议详情', icon: '', identify: 'pop:protocol:detail', activeMenu: ['/protocol/protocol_rebate'], noCache: true },
        component: () =>
          import(
            '@/subject/pop/views/protocol/protocol_rebate/protocol_detail'
            ),
        hidden: true
      },
      {
        path: 'supplement_detail/:supplementId',
        name: 'SupplementDetail',
        meta: {
          title: '补充协议详情',
          icon: '',
          identify: 'pop:protocol:supplement_detail',
          activeMenu: ['/protocol/protocol_rebate'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/pop/views/protocol/protocol_rebate/supplement_detail'
            ),
        hidden: true
      },
      {
        path: 'supplement_detail_record/:supplementId/:agreementSnapshotId',
        name: 'SupplementDetailRecord',
        meta: {
          title: '补充协议详情',
          icon: '',
          identify: 'pop:protocol:supplement_detail_record',
          activeMenu: ['/protocol/protocol_rebate'],
          noCache: true
        },
        component: () =>
          import(
            '@/subject/pop/views/protocol/protocol_rebate/supplement_detail_record'
            ),
        hidden: true
      },
      {
        path: 'protocol_edit/:customerEid/:agreementId/:agreementType',
        name: 'ProtocolEdit',
        meta: {
          title: '修改协议',
          icon: '',
          identify: 'pop:protocol:protocol_edit',
          activeMenu: ['/protocol/protocol_rebate'],
          noCache: true
        },
        component: () =>
          import('@/subject/pop/views/protocol/protocol_rebate/protocol_edit'),
        hidden: true
      },
      {
        path: 'protocol_add/:customerEid',
        name: 'ProtocolAdd',
        meta: { title: '新增协议', icon: '', identify: 'pop:protocol:add', activeMenu: ['/protocol/protocol_rebate'], noCache: true },
        component: () =>
          import('@/subject/pop/views/protocol/protocol_rebate/protocol_add'),
        hidden: true
      }
    ]
  }
]

export default route
