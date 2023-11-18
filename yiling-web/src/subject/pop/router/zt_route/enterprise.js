const route = [
  {
    path: '/zt_enterprise',
    alwaysShow: true,
    name: '',
    redirect: '/zt_enterprise/zt_enterprise_information',
    meta: { 
      title: '企业信息管理', 
      icon: 'icon1', 
      identify: 'zt:zt_enterprise' 
    },
    children: [
      {
        path: 'zt_enterprise_information',
        name: 'ZtEnterpriseInformation',
        meta: { 
          title: '企业信息', 
          icon: '', 
          identify: 'zt:zt_enterprise:zt_enterprise_information' 
        },
          component: () => import('@/subject/pop/views_zt/zt_enterprise/zt_enterprise_information/index')
      },
      {
        path: 'zt_enterprise_modify',
        name: 'ZtEnterpriseModify',
        meta: { 
          title: '企业信息修改', 
          icon: '', 
          identify: 'zt:zt_enterprise:zt_enterprise_modify',
          activeMenu: ['/zt_enterprise/zt_enterprise_information'],
          noCache: true
        },
          component: () => import('@/subject/pop/views_zt/zt_enterprise/zt_enterprise_modify/index'),
          hidden: true
      }
    ]
  }
]
export default route