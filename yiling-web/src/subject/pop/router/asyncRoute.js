import Layout from '@/subject/pop/layout';
import _ from 'lodash';

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
    children: [
      {
        path: 'redirect/:path(.*)',
        meta: { noCache: true },
        hidden: true,
        component: () => import('@/common/views/redirect/index')
      },
      {
        path: 'importFile/importFile_index',
        name: 'importFile_index',
        meta: { title: '导入', activeMenu: ['*'], noCache: true },
        hidden: true,
        component: () => import('@/subject/pop/views/import_file/index')
      },
      {
        path: '/403',
        meta: { title: '无权限', activeMenu: ['*'], noCache: true },
        hidden: true,
        component: () => import('@/common/views/403')
      },
      {
        path: 'change_phone',
        name: 'change_phone',
        meta: { title: '手机号换绑' },
        component: () => import('@/subject/pop/views/account_manager/change_phone/index.vue'),
        hidden: true
      },
      {
        path: 'forget_pass',
        name: 'forget_pass',
        meta: { title: '重置登陆密码' },
        component: () => import('@/subject/pop/views/account_manager/forget_pass/index.vue'),
        hidden: true
      }
    ]
  },
  {
    path: '/dashboard',
    component: Layout,
    redirect: '/dashboard/dashboard',
    meta: { identify: 'pop:index' },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/subject/pop/views/dashboard/index'),
        meta: { title: '首页', icon: 'icon0', identify: 'pop:index:index', noCache: true }
      }
    ]
  },
  {
    path: '/b2b_dashboard',
    component: Layout,
    redirect: '/b2b_dashboard/b2b_dashboard',
    meta: { identify: 'b2b:index' },
    children: [
      {
        path: 'b2b_dashboard',
        name: 'B2bDashboard',
        component: () => import('@/subject/pop/views_b2b/dashboard/index'),
        meta: { title: '首页', icon: 'icon0', identify: 'b2b:index:index', noCache: true }
      }
    ]
  },
  {
    path: '/zt_dashboard',
    component: Layout,
    redirect: '/zt_dashboard/zt_dashboard',
    meta: { identify: 'zt:index' },
    children: [
      {
        path: 'zt_dashboard',
        name: 'ZtDashboard',
        component: () => import('@/subject/pop/views_zt/dashboard/index'),
        meta: { title: '首页', icon: 'icon0', identify: 'zt:index:index', noCache: true }
      }
    ]
  },
  {
    path: '/sale_dashboard',
    component: Layout,
    redirect: '/sale_dashboard/sale_dashboard',
    meta: { identify: 'sale:index' },
    children: [
      {
        path: 'sale_dashboard',
        name: 'SaleDashboard',
        component: () => import('@/subject/pop/views_sale/dashboard/index'),
        meta: { title: '首页', icon: 'icon0', identify: 'sale:index:index', noCache: true }
      }
    ]
  }
];

export function getAsyncRoutes() {
  return _.cloneDeep(asyncRoutes.concat(moduleRoutes));
}
