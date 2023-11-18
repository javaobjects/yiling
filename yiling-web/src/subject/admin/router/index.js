import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/**
 * constantRoutes
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/subject/admin/views/login/index'),
    hidden: true
  }
]

export const endRoutes = [
  // 404 放在数组最后
  {
    path: '*',
    redirect: '/403',
    hidden: true
  }
]

const createRouter = () => new Router({
  mode: 'hash',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
