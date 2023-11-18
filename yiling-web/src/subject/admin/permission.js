import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, getCookieByKey, getPlatform } from './utils/auth'
import settings from './settings'
import { error, log } from './utils/index'

NProgress.configure({ showSpinner: false })

// 白名单路由
const whiteList = ['/login']

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = settings.title

  const hasToken = store.getters.token || getToken()
  const company = store.getters.userCompany || getCookieByKey('YY_USER_COMPANY')
  log('YY_USER_COMPANY：', company)
  log('YY_USER_INFO：', getCookieByKey('YY_USER_INFO'))

  if (hasToken) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      let platform = store.getters.platform || getPlatform()
      const routes = store.state.permission.addRoutes
      log('---get platform ---' + platform)
      if (routes && routes.length) {
        next()
      } else {
        try {
          if (!platform) {
            platform = await store.dispatch('app/getPlatform')
          }
          const accessRoutes = await store.dispatch('permission/initRoutes', platform)
          router.addRoutes(accessRoutes)
          let routeStr = ''
          if (process.env.NODE_ENV === 'dev') {
            routeStr = to.path
          } else {
            if (accessRoutes.length > 1 && typeof accessRoutes[1].children !== 'undefined') {
              for (let i = 0; i< accessRoutes[1].children.length; i++) {
                const item = accessRoutes[1].children[i]
                if (!item.hidden) {
                  routeStr = accessRoutes[1].path + '/' + item.path
                  break
                }
              }
            }
          }
          if (routeStr) {
            next({ path: routeStr, replace: true })
          } else {
            next({ ...to, replace: true })
          }
        } catch (e) {
          log(e)
          error('获取菜单失败，请重新再试')
          NProgress.done()
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // next(`/login?redirect=${to.path}`)
      next('/login?redirect=/')
      NProgress.done()
    }
  }
})

router.afterEach(async (to, from, next) => {
  const identify = to.meta && to.meta.identify ? to.meta.identify : ''
  if (!from.hidden) {
    sessionStorage.setItem('FROM_ROUTER_PATH', from.path)
  }
  sessionStorage.setItem('TO_ROUTER_IDENTIFY', identify)
  const identifyArray = identify.split(':')
  if (identifyArray.length > 2) {
    const routes = store.state.permission.addRoutes.find(item => item.meta && item.meta.identify === `${identifyArray[0]}:${identifyArray[1]}`)
    store.dispatch('permission/setCurrentButton', routes ? routes.buttons : [])
  } else {
    store.dispatch('permission/setCurrentButton', [])
  }
  NProgress.done()
})
