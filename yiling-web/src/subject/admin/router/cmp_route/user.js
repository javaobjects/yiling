const route = [
  {
    path: '/user',
    redirect: '/user/user_register',
    name: 'User',
    meta: { title: '用户管理', icon: 'cmp_user', identify: 'cmp:user' },
    children: [
      {
        path: 'user_register',
        name: 'UserRegister',
        meta: {
          title: '注册用户',
          icon: '',
          identify: 'cmp:user:user_register'
        },
        component: () => import('@/subject/admin/views_cmp/user/user_register')
      },
      {
        path: 'patient_data',
        name: 'PatientData',
        meta: {
          title: '就诊人数据',
          icon: '',
          identify: 'cmp:user:patient_data'
        },
        component: () => import('@/subject/admin/views_cmp/user/patient_data')
      }
      // {
      //   path: 'registration_channel',
      //   name: 'RegistrationChannel',
      //   meta: {
      //     title: '注册渠道管理',
      //     icon: '',
      //     identify: 'cmp:user:registration_channel'
      //   },
      //   component: () => import('@/subject/admin/views_cmp/user/registration_channel')
      // }
    ]
  }
]
export default route