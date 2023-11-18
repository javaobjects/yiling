const route = [
  {
    path: '/quality',
    redirect: '/quality/basic_drugs_index',
    name: '',
    meta: { title: '质量管理', icon: 'icon7', identify: 'zt:quality' },
    children: [
      {
        path: 'basic_drugs_index',
        name: 'BasicDrugsIndex',
        meta: { title: '基础药品管理', icon: '', identify: 'zt:quality:basic_drugs_index' },
        component: () => import('@/subject/admin/views_zt/quality/basic_drugs_list/index')
      },
      {
        path: 'basic_drugs_edit/:id/:type',
        name: 'BasicDrugsEdit',
        meta: { 
          title: '药品详情', 
          icon: '', 
          identify: 'zt:quality:basic_drugs_edit', 
          activeMenu: ['*'], 
          noCache: true 
        },
        component: () => import('@/subject/admin/views_zt/quality/basic_drugs_edit'),
        hidden: true
      },
      {
        path: 'drugs_class_index',
        name: 'DrugsClassIndex',
        meta: { title: '药品分类管理', icon: '', identify: 'zt:quality:drugs_class_index' },
        component: () => import('@/subject/admin/views_zt/quality/drugs_class_list/index')
      },
      {
        path: 'goods_to_standard_library',
        name: 'GoodsToStandardLibrary',
        meta: { title: '商品对应标准库匹配', icon: '', identify: 'zt:quality:goods_to_standard_library' },
        component: () => import('@/subject/admin/views_zt/quality/goods_to_standard_library/index')
      },
      {
        path: 'goods_label',
        name: 'GoodsLabel',
        meta: { title: '商品标签管理', icon: '', identify: 'zt:quality:goods_label' },
        component: () => import('@/subject/admin/views_zt/quality/goods_label/index')
      },
      {
        path: 'goods_label_add',
        name: 'GoodsLabelAdd',
        meta: {
          title: '新建标签',
          icon: '',
          identify: 'zt:quality:goods_label_add',
          activeMenu: ['/quality/goods_label'],
          noCache: true
        },
        component: () => import('@/subject/admin/views_zt/quality/goods_label/goods_label_add/index'),
        hidden: true
      }
    ]
  }
]

export default route
