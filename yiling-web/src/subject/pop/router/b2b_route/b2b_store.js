const route = [
  {
    path: '/b2b_store',
    alwaysShow: true,
    redirect: '/b2b_store/store_manage',
    name: '',
    meta: {
      title: '店铺管理',
      icon: 'store_manage',
      identify: 'b2b:b2b_store',
      roles: []
    },
    children: [
      {
        path: 'store_manage',
        name: 'StoreManage',
        meta: { title: '店铺设置', icon: '', identify: 'b2b:b2b_store:store_manage', noCache: true },
        component: () => import('@/subject/pop/views_b2b/b2b_store/store_manage')
      },
      {
        path: 'store_announce',
        name: 'StoreAnnounce',
        meta: { title: '店铺公告', icon: '', identify: 'b2b:b2b_store:store_announce', noCache: true },
        component: () => import('@/subject/pop/views_b2b/b2b_store/store_announce')
      },
      {
        path: 'floor_manage',
        name: 'FloorManage',
        meta: { 
          title: '楼层管理', 
          icon: '', 
          identify: 'b2b:b2b_store:floor_manage'
        },
        component: () => import('@/subject/pop/views_b2b/b2b_store/floor_manage')
      },
      {
        path: 'floor_manage_establish/:type?/:id',
        name: 'FloorManageEstablish',
        meta: {
          title: '新建/编辑楼层',
          icon: '',
          identify: 'b2b:b2b_store:floor_manage_establish',
          activeMenu: ['/b2b_store/floor_manage'],
          noCache: true
        },
        component: () => import('@/subject/pop/views_b2b/b2b_store/floor_manage/establish'),
        hidden: true
      }
    ]
  }
]

export default route
