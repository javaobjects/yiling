const route = [
  {
    path: '/feedback',
    redirect: '/feedback/feedback_list',
    name: 'Feedback',
    meta: { title: '使用反馈', icon: 'feedback', identify: 'content:feedback' },
    children: [
      {
        path: 'feedback_list',
        name: 'FeedbackList',
        meta: {
          title: '反馈列表',
          icon: '',
          identify: 'content:feedback:feedback_list'
        },
        component: () => import('@/subject/admin/views_content/feedback/feedback_list')
      }
    ]
  }
]
export default route