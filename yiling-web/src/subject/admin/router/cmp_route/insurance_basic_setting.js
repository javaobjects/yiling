const route = [
  {
    path: '/insurance_basic_setting',
    redirect: '/insurance_basic_setting/insurance_drug_manage',
    name: '',
    meta: { title: '保险基础设置', icon: 'insurance_basic_setting', identify: 'cmp:insure_basic_setting' },
    children: [
      // 保险药品管控
      {
        path: 'insurance_drug_manage',
        name: 'InsuranceDrugManage',
        meta: { title: '保险药品管控', icon: '', identify: 'cmp:insure_basic_setting:insurance_drug_manage' },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/insurance_drug_manage/drug_manage/index')
      },
      {
        path: 'insurance_channel_manage/:id',
        name: 'InsuranceChannelManage',
        meta: { title: '保险药品渠道管控', icon: '', identify: 'cmp:insure_basic_setting:insurance_channel_manage', activeMenu: ['/insurance_basic_setting/insurance_drug_manage'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/insurance_drug_manage/channel_manage/index'),
        hidden: true
      },
      // 保险服务商
      {
        path: 'service_provider_manage',
        name: 'ServiceProviderManage',
        meta: { title: '保险服务商管理', icon: '', identify: 'cmp:insure_basic_setting:service_provider_manage' },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/insurance_service_provider/provider_manage/index')
      },
      {
        path: 'add_service_provider',
        name: 'AddServiceProvider',
        meta: { title: '保险管理', icon: '', identify: 'cmp:insure_basic_setting:add_service_provider', activeMenu: ['/insurance_basic_setting/service_provider_manage'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/insurance_service_provider/add_provider/index'),
        hidden: true
      },
      {
        path: 'edit_service_provider/:id',
        name: 'EditServiceProvider',
        meta: { title: '保险管理', icon: '', identify: 'cmp:insure_basic_setting:edit_service_provider', activeMenu: ['/insurance_basic_setting/service_provider_manage'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/insurance_service_provider/edit_provider/index'),
        hidden: true
      },
      {
        path: 'service_provider_content/:id/:companyName',
        name: 'ServiceProviderContent',
        meta: { title: '保险内容管理', icon: '', identify: 'cmp:insure_basic_setting:service_provider_content', activeMenu: ['/insurance_basic_setting/service_provider_manage'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/insurance_service_provider/content_manage/index'),
        hidden: true
      },
      {
        path: 'add_service_provider_content/:id/:companyName',
        name: 'AddServiceProviderContent',
        meta: { title: '添加保险服务内容', icon: '', identify: 'cmp:insure_basic_setting:add_service_provider_content', activeMenu: ['/insurance_basic_setting/service_provider_manage'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/insurance_service_provider/add_content/index'),
        hidden: true
      },
      // 药品商家提成设置
      {
        path: 'drug_merchants_commission',
        name: 'DrugMerchantsCommission',
        meta: { title: '药品商家提成设置', icon: '', identify: 'cmp:insure_basic_setting:drug_merchants_commission' },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/drug_merchants_commission/drug_merchants/index')
      },
      {
        path: 'merchants_commission_manage/:eid',
        name: 'MerchantsCommissionManage',
        meta: { title: '提成设置', icon: '', identify: 'cmp:insure_basic_setting:merchants_commission_manage', activeMenu: ['/insurance_basic_setting/drug_merchants_commission'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/drug_merchants_commission/commission_manage/index'),
        hidden: true
      },
      {
        path: 'merchants_account_manage/:id',
        name: 'MerchantsAccountManage',
        meta: { title: '结账账号设置', icon: '', identify: 'cmp:insure_basic_setting:merchants_account_manage', activeMenu: ['/insurance_basic_setting/drug_merchants_commission'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/drug_merchants_commission/account_manage/index'),
        hidden: true
      },
      {
        path: 'drug_merchants_detail/:id',
        name: 'DrugMerchantsDetail',
        meta: { title: '药品商家提成详情', icon: '', identify: 'cmp:insure_basic_setting:drug_merchants_detail', activeMenu: ['/insurance_basic_setting/drug_merchants_commission'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/drug_merchants_commission/drug_merchants_detail/index'),
        hidden: true
      },
      {
        path: 'add_drug_merchants',
        name: 'AddDrugMerchants',
        meta: { title: '添加药品商家', icon: '', identify: 'cmp:insure_basic_setting:add_drug_merchants', activeMenu: ['/insurance_basic_setting/drug_merchants_commission'], noCache: true },
        component: () => import('@/subject/admin/views_cmp/insurance_basic_setting/drug_merchants_commission/add_merchants/index'),
        hidden: true
      }
    ]
  }
]

export default route