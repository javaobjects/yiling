import Layout from '@/subject/admin/layout';
import _ from 'lodash'

let moduleRoutes = []

const modulesFiles = require.context('../router', true, /\.js$/)
modulesFiles.keys().reduce((modules, modulePath) => {
  if (modulePath === './asyncRoute.js' || modulePath === './index.js') return
  const value = modulesFiles(modulePath)
  const router = value.default.map(item => {
    item.component = Layout
    return item
  })
  moduleRoutes = moduleRoutes.concat(router)
}, {})

const asyncRoutes = [
  {
    path: '/',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'redirect/:path(.*)',
        component: () => import('@/common/views/redirect/index')
      },
      {
        path: 'importFile/importFile_index',
        name: 'ImportFileIndex',
        meta: { title: '导入', activeMenu: ['*'], noCache: true },
        component: () => import('@/subject/admin/views/importFile/index')
      },
      {
        path: '/403',
        meta: { title: '无权限', activeMenu: ['*'], noCache: true },
        component: () => import('@/common/views/403')
      },
      {
        path: '/editPass',
        meta: { title: '修改密码', noCache: true },
        component: () => import('@/subject/admin/views/account_manager/forget_pass/index'),
        hidden: true
      }
    ]
  },
  {
    path: '/dashboard',
    component: Layout,
    redirect: '/dashboard/dashboard',
    meta: { identify: 'sys:index' },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/subject/admin/views/dashboard/index'),
        meta: { title: '首页', icon: 'icon0', identify: 'sys:index:index', noCache: true }
      }
    ]
  },
  {
    path: '/sale_dashboard',
    component: Layout,
    redirect: '/sale_dashboard/sale_dashboard',
    meta: { identify: 'sale:sale_index' },
    children: [
      {
        path: 'sale_dashboard',
        name: 'SaleDashboard',
        component: () => import('@/subject/admin/views_sale/dashboard/index'),
        meta: { title: '首页', icon: 'icon0', identify: 'sale:sale_index:index', noCache: true }
      }
    ]
  }
];

export function getAsyncRoutes() {
  return _.cloneDeep(asyncRoutes.concat(moduleRoutes))
}
