const route = [
  {
    path: '/meeting',
    redirect: '/meeting/meeting_list',
    name: 'Meeting',
    meta: { title: '会议活动管理', icon: 'meeting', identify: 'content:meeting' },
    children: [
      {
        path: 'meeting_list',
        name: 'MeetingList',
        meta: {
          title: '会议管理',
          icon: '',
          identify: 'content:meeting:meeting_list'
        },
        component: () => import('@/subject/admin/views_content/meeting/meeting_list')
      },
      {
        path: 'add_edit_meeting/:id?/:type',
        name: 'AddEditMeeting',
        meta: {
          title: '添加/编辑会议',
          icon: '',
          identify: 'content:meeting:add_edit_meeting',
          activeMenu: ['/meeting/meeting_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_content/meeting/meeting_list/add_edit_meeting')
      }
    ]
  }
]
export default route