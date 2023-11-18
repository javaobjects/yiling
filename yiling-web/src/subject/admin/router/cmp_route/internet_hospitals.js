const route = [
  {
    path: '/internet_hospitals',
    redirect: '/internet_hospitals/terminal_business',
    name: 'InternetHospitals',
    meta: { 
      title: '互联网医院', 
      icon: 'internet_hospitals', 
      identify: 'cmp:internet_hospitals' 
    },
    children: [
      {
        path: 'terminal_business',
        name: 'TerminalBusiness',
        meta: {
          title: '终端商家',
          icon: '',
          identify: 'cmp:internet_hospitals:terminal_business'
        },
        component: () => import('@/subject/admin/views_cmp/internet_hospitals/terminal_business')
      }
    ]
  }
]
export default route