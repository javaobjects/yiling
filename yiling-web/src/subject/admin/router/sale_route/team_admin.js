const route = [
    {
      path: '/sell_team',
      redirect: '/sell_team/team_admin',
      name: '',
      meta: { title: '团队管理', icon: 'icon14', identify: 'sale:sale_team' },
      children: [
        {
          path: 'team_admin',
          name: 'TeamAdmin',
          meta: { title: '团队成员', icon: '', identify: 'sale:sale_team:sale_team_admin' },
          component: () => import('@/subject/admin/views_sale/sell_team/team_admin/index')
        },
        {
          path: 'team_admin_detail/:id',
          name: 'TeamAdminDetail',  
          meta: { title: '拓客详情', icon: '', identify: 'sale:sale_team:sale_team_admin_detail', activeMenu: ['*'] },
          component: () => import('@/subject/admin/views_sale/sell_team/team_admin_detail/index'),
          hidden: true
        }
        
      ]
    }
  ]
  
  export default route
  