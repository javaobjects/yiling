const route = [
  {
    path: '/cmp_activity',
    redirect: '/cmp_activity/patient_education',
    name: '',
    meta: { title: '活动管理', icon: 'cmp_activity', identify: 'cmp:cmp_activity' },
    children: [
      {
        path: 'patient_education',
        name: 'PatientEducation',
        meta: { 
          title: '患者教育活动', 
          icon: '', 
          identify: 'cmp:cmp_activity:patient_education' 
        },
          component: () => import('@/subject/admin/views_cmp/activity/education/patient_education/index')
      },
      {
        path: 'add_activity/:id',
        name: 'AddActivity',
        meta: { 
          title: '添加活动',
          icon: '', 
          identify: 'cmp:cmp_activity:add_activity',
          activeMenu: ['/cmp_activity/patient_education'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/education/add_activity/index')
      },
      {
        path: 'activity_doctor/:id',
        name: 'ActivityDoctor',
        meta: { 
          title: '活动医生', 
          icon: '', 
          identify: 'cmp:cmp_activity:activity_doctor',
          activeMenu: ['/cmp_activity/patient_education'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/education/activity_doctor/index')
      },
      {
        path: 'doctor_patient',
        name: 'DoctorPatient',
        meta: { 
          title: '医带患活动', 
          icon: '', 
          identify: 'cmp:cmp_activity:doctor_patient' 
        },
          component: () => import('@/subject/admin/views_cmp/activity/doctor_patient')
      },
      {
        path: 'activity_establish/:id?/:type',
        name: 'ActivityEstablish',
        meta: { 
          title: '创建/编辑活动', 
          icon: '', 
          identify: 'cmp:cmp_activity:activity_establish',
          activeMenu: ['/cmp_activity/doctor_patient'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/doctor_patient/establish')
      },
      {
        path: 'see_doctor/:id',
        name: 'SeeDoctor',
        meta: { 
          title: '查看医生', 
          icon: '', 
          identify: 'cmp:cmp_activity:see_doctor',
          activeMenu: ['/cmp_activity/doctor_patient'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/doctor_patient/see_doctor')
      },
      {
        path: 'view_patients/:id',
        name: 'ViewPatient',
        meta: { 
          title: '查看患者', 
          icon: '', 
          identify: 'cmp:cmp_activity:view_patients',
          activeMenu: ['/cmp_activity/doctor_patient'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/doctor_patient/view_patients')
      },
      {
        path: 'view_patients_establish/:id',
        name: 'ViewPatientsEstablish',
        meta: { 
          title: '审核/查看凭证', 
          icon: '', 
          identify: 'cmp:cmp_activity:view_patients_establish',
          activeMenu: ['/cmp_activity/doctor_patient'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/doctor_patient/view_patients/establish')
      },
      {
        path: 'goods_promotion_list',
        name: 'GoodsPromotionList',
        meta: { 
          title: '商品推广销售活动', 
          icon: '', 
          identify: 'cmp:cmp_activity:goods_promotion_list' 
        },
          component: () => import('@/subject/admin/views_cmp/activity/goods_promotion/goods_promotion_list/index')
      },
      {
        path: 'add_goods_promotion/:id',
        name: 'AddGoodsPromotion',
        meta: { 
          title: '添加活动',
          icon: '', 
          identify: 'cmp:cmp_activity:add_goods_promotion',
          activeMenu: ['/cmp_activity/goods_promotion_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/goods_promotion/add_goods_promotion/index')
      },
      {
        path: 'goods_promotion_doctor/:id',
        name: 'GoodsPromotionDoctor',
        meta: { 
          title: '活动医生', 
          icon: '', 
          identify: 'cmp:cmp_activity:goods_promotion_doctor',
          activeMenu: ['/cmp_activity/goods_promotion_list'],
          noCache: true
        },
        hidden: true,
        component: () => import('@/subject/admin/views_cmp/activity/goods_promotion/goods_promotion_doctor/index')
      }
    ]
  }
]

export default route