const route = [
  {
    path: '/question_handling',
    redirect: '/question_handling/question_list',
    name: 'QuestionHandling',
    meta: { title: '疑问处理', icon: 'question_handling', identify: 'content:question_handling' },
    children: [
      {
        path: 'question_list',
        name: 'QuestionList',
        meta: {
          title: '问题知识库',
          icon: '',
          identify: 'content:question_handling:question_list'
        },
        component: () => import('@/subject/admin/views_content/question_handling/question_list')
      },
      {
        path: 'add_edit_question/:id?/:type',
        name: 'AddEditQuestion',
        meta: {
          title: '添加/编辑疑问',
          icon: '',
          identify: 'content:question_handling:add_edit_question',
          activeMenu: ['/question_handling/question_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/question_handling/question_list/add_edit_question')
      },
      {
        path: 'community_list',
        name: 'CommunityList',
        meta: {
          title: '医代问题社区',
          icon: '',
          identify: 'content:question_handling:community_list'
        },
        component: () => import('@/subject/admin/views_content/question_handling/community_list')
      },
      {
        path: 'reply/:id?',
        name: 'Reply',
        meta: {
          title: '回复',
          icon: '',
          identify: 'content:question_handling:reply',
          activeMenu: ['/question_handling/community_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/question_handling/community_list/reply')
      },
      {
        path: 'community_details/:id',
        name: 'CommunityDetails',
        meta: {
          title: '医代问题社区详情',
          icon: '',
          identify: 'content:question_handling:community_details',
          activeMenu: ['/question_handling/community_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/question_handling/community_list/details')
      }
    ]
  }
]
export default route