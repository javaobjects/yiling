import router from './router'
import store from './store'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {getToken, getCookieByKey, getPlatform, getCurrentUser, keys } from './utils/auth'
import { error, log, goLogin } from '@/subject/pop/utils/index'

NProgress.configure({ showSpinner: false })

// 白名单路由
const whiteList = ['/login', '/forgetPass']

router.beforeEach(async (to, from, next) => {
  NProgress.start()
  const hasToken = store.getters.token || getToken()
  const company = store.getters.userCompany || getCookieByKey(keys.companyKey)
  const userInfo = store.getters.userInfo || getCurrentUser()
  log('TO：', to)
  log('USER_INFO：', userInfo)
  if (hasToken && company && userInfo) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      let platform = store.getters.platform || getPlatform()
      const routes = store.state.permission.addRoutes
      log('---get platform ---' + platform)
      log(routes)
      if (routes && routes.length) {
        next()
      } else {
        try {
          const tabPlatform = to.query.tab_platform || ''
          if (tabPlatform) {
            const topList = await store.dispatch('app/getTopList')
            if (topList.find(item => item.appId == tabPlatform)) {
              platform = tabPlatform
              await store.dispatch('app/togglePlatform', platform)
            }
          } else {
            if (!platform) {
              platform = await store.dispatch('app/getPlatform')
            }
          }
          log('获取到2-platform=' + platform)
          const accessRoutes = await store.dispatch('permission/initRoutes', platform)
          router.addRoutes(accessRoutes)
          log('accessRoutes', accessRoutes)
          let routeStr = tabPlatform ? (to.fullPath || '') : ''
          if (process.env.NODE_ENV === 'dev') {
            routeStr = routeStr || to.path
          }
          if (!routeStr && accessRoutes.length > 1 && typeof accessRoutes[1].children !== 'undefined') {
            for (let i= 0; i< accessRoutes[1].children.length; i++) {
              const item = accessRoutes[1].children[i]
              if (!item.hidden) {
                routeStr = accessRoutes[1].path + '/' + item.path
                break
              }
            }
          }
          log(routeStr)
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
      // next(`/login?redirect=/`)
      if (process.env.NODE_ENV === 'dev') {
        next('/login?redirect=/')
      } else {
        goLogin()
      }
      NProgress.done()
    }
  }
  store.dispatch('tagsView/addCachedView', to)
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
