import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/**
 * 路由配置项
 *
 // 当设置 true 的时候该路由不会在侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 hidden: true // (默认 false)
 //当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 redirect: 'noRedirect'
 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 alwaysShow: true
 name: 'router-name' // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 meta: {
  title: 'title' // 设置该路由在侧边栏和面包屑中展示的名字
  icon: 'svg-name' // 设置该路由的图标，支持 svg-class
  noCache: true // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
  // 当路由设置了该属性，则会高亮相对应的侧边栏。
  // 这在某些场景非常有用，比如：一个文章的列表页路由为：/article/list
  // 点击文章进入文章详情页，这时候路由为/article/1，但你想在侧边栏高亮文章列表的路由，就可以进行如下设置，支持多个
  // 也支持所有，示例：activeMenu: ['*']
  activeMenu: ['/article/list']
}
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/subject/pop/views/login/index'),
    hidden: true
  },
  {
    path: '/forgetPass',
    component: () => import('@/subject/pop/views/login/forgetPass'),
    hidden: true
  }
]

export const endRoutes = [
  // 403 放在数组最后
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
