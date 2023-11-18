const route = [
  {
    path: '/agreement',
    redirect: '/agreement/factory_list',
    name: '',
    meta: { title: '协议管理', icon: 'agreement', identify: 'pop:agreement' },
    children: [
      {
        path: 'factory_list',
        name: 'FactoryList',
        meta: { title: '厂家管理', icon: '', identify: 'pop:agreement:factory_list'},
        component: () => import('@/subject/pop/views/agreement/factory_manage/factory_list')
      },
      {
        path: 'agreement_creat',
        name: 'AgreementCreat',
        meta: { title: '协议新建', icon: '', identify: 'pop:agreement:agreement_creat', noCache: true},
        component: () => import('@/subject/pop/views/agreement/agreement_manage/agreement_creat')
      },
      {
        path: 'agreement_audit',
        name: 'AgreementAudit',
        meta: { title: '协议审核', icon: '', identify: 'pop:agreement:agreement_audit'},
        component: () => import('@/subject/pop/views/agreement/agreement_manage/agreement_audit')
      },
      {
        path: 'agreement_list',
        name: 'AgreementList',
        meta: { title: '协议列表', icon: '', identify: 'pop:agreement:agreement_list'},
        component: () => import('@/subject/pop/views/agreement/agreement_manage/agreement_list')
      },
      {
        path: 'agreement_detail/:id/:operateType',
        name: 'AgreementDetail',
        meta: { title: '协议详情', icon: '', identify: 'pop:agreement:agreement_detail', activeMenu: ['*'], noCache: true},
        component: () => import('@/subject/pop/views/agreement/agreement_manage/agreement_detail'),
        hidden: true
      }
    ]
  }
]

export default route
