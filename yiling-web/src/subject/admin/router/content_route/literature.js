const route = [
  {
    path: '/literature',
    redirect: '/literature/literature_list',
    name: 'Literature',
    meta: { title: '文献管理', icon: 'literature', identify: 'content:literature' },
    children: [
      {
        path: 'literature_list',
        name: 'LiteratureList',
        meta: {
          title: '文献管理',
          icon: '',
          identify: 'content:literature:literature_list'
        },
        component: () => import('@/subject/admin/views_content/literature/literature_list')
      },
      {
        path: 'add_edit/:id?/:type',
        name: 'AddEdit',
        meta: {
          title: '添加/编辑文献',
          icon: '',
          identify: 'content:literature:add_edit',
          activeMenu: ['/literature/literature_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/literature/add_edit')
      },
      {
        path: 'classification_settings',
        name: 'ClassificationSettings',
        meta: {
          title: '文献栏目设置',
          icon: '',
          identify: 'content:literature:classification_settings'
        },
        component: () => import('@/subject/admin/views_content/literature/classification_settings')
      }
    ]
  }
]
export default route