import { constantRoutes, endRoutes } from '@/subject/pop/router'
import { getAsyncRoutes } from '@/subject/pop/router/asyncRoute'
import { getUserSideRouter } from '@/subject/pop/api/user'

// 查询是否需要渲染
function hasPermission(routes, identify) {
  if (routes.length && identify) {
    let arr = routes.filter(route => route.meta && route.meta.identify && route.meta.identify === identify)
    return arr.length ? arr[0] : false
  }
  return false
}

// 递归处理多级数据
function handleChildren(routeChildren, apiChildren) {
  let array = []
  if (routeChildren.length && apiChildren.length) {
    apiChildren.forEach(child => {
      let hasChild = hasPermission(routeChildren, child.menuIdentification)
      if (hasChild) {
        hasChild.meta.title = child.menuName
        hasChild.buttons = child.children || []
        array.push(hasChild)
      }
    })
  }
  return array
}

// 处理按钮
function handleButton(route) {
  let array = []
  route.forEach(item => {
    if (!item.hidden) {
      array = array.concat(item.buttons || [])
    }
  })
  return array
}

// 路由处理
export function initAsyncRoutes(datas = []) {
  let asyncRoutes = getAsyncRoutes()
  let result = [asyncRoutes[0]]
  datas.forEach(item => {
    let hasRoute = hasPermission(asyncRoutes, item.menuIdentification)
    if (hasRoute) {
      hasRoute.meta.title = item.menuName
      let array = hasRoute.children.filter(route => route.hidden === true)
      let other = []
      if (item.children && item.children.length && hasRoute.children && hasRoute.children.length) {
        other = handleChildren(hasRoute.children, item.children)
      }
      const buttons = handleButton(other)
      hasRoute.children = array.concat(other)
      hasRoute.buttons = buttons
      result.push(hasRoute)
    }
  })
  return result
}

const state = {
  // 全部路由
  routes: [],
  // 新增路由
  addRoutes: [],
  // 当前按钮组合
  currentButton: [],
  // 是否有首页
  hasDashboard: false
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = routes.concat(constantRoutes)
    state.hasDashboard = !!routes.find(
      item => item.path &&
      (item.path === '/dashboard' || item.path === '/b2b_dashboard' || item.path === '/zt_dashboard' || item.path === '/sale_dashboard') &&
      item.children.length
    )
  },
  SET_CURRENT_BUTTONS: (state, routes) => {
    state.currentButton = Object.assign([], routes)
  }
}

const actions = {
  initRoutes({ commit }, appId) {
    return new Promise((resolve, reject) => {
      getUserSideRouter(appId).then(data => {
        let accessedRoutes = []
        // 开发模式写死路由，非开发模式由后台控制
        const test = sessionStorage.getItem('ROUTE_TEST_MODEL') == 1
        if (process.env.NODE_ENV === 'dev' && !test) {
          accessedRoutes = getAsyncRoutes()
        } else {
          accessedRoutes = initAsyncRoutes(data ? data.list : [])
        }
        accessedRoutes = accessedRoutes.concat(endRoutes)
        console.log(accessedRoutes,99)
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      }).catch(error => {
        reject(error)
      })
    })
  },
  setCurrentButton({ commit }, routes) {
    commit('SET_CURRENT_BUTTONS', routes)
  },
  setRoutes({ commit }, routes) {
    commit('SET_ROUTES', routes || [])
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
