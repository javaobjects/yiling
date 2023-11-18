const route = [
  {
    path: '/account_manager',
    redirect: '/account_manager/index',
    name: '',
    meta: { title: '账号管理', icon: 'icon4', identify: 'sys:account_manager' },
    children: [
      {
        path: 'forget_pass',
        name: 'ForgetPass',
        meta: { title: '重置登录密码', icon: '', identify: 'sys:account_manager:forget_pass'},
        component: () => import('@/subject/admin/views/account_manager/forget_pass/index')
      }
    ],
    hidden: true
  }
]

export default route
