const route = [
    {
      path: '/cmp_admin_manager',
      alwaysShow: true,
      redirect: '/cmp_admin_manager/cmp_staff_manager',
      name: '',
      meta: { title: '员工管理cmp', icon: 'icon26', identify: 'cmp:admin_manager' },
      children: [
        {
          path: 'cmp_staff_manager',
          name: 'CmpStaffManager',
          meta: { title: '销售人员管理', icon: '', identify: 'cmp:admin_manager:staff_manager' },
          component: () => import('@/subject/pop/views_cmp/adminManager/staffManager/index')
        }
        // {
        //   path: "cmp_staff_manager_add",
        //   name: "CmpStaffManagerAdd",
        //   meta: { title: "销售人员添加", icon: "", identify: "cmp:admin_manager:staff_manager_add" },
        //   component: () => import("@/subject/pop/views_zt/roles/user/index"),
        //   hidden: true
        // },
      ]
    }
  ];
  
  export default route;
  