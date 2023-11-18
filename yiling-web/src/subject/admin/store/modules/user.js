import { login, logout } from '@/subject/admin/api/user'
import { getToken, setToken, removeToken, setCookieByKey, getCookieByKey, removeCookieByKey, removePlatform } from '@/subject/admin/utils/auth'
import { resetRouter } from '@/subject/admin/router'
import store from '@/subject/admin/store'

const getDefaultState = () => {
  return {
    token: getToken() || '',
    userInfo: getCookieByKey('YY_USER_INFO') || {}
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_INFO: (state, obj) => {
    state.userInfo = Object.assign(state.userInfo, obj)
  }
}

const actions = {
  // 登录
  login({ commit }, userInfo) {
    const { userName, password, smsCode, code } = userInfo
    return new Promise((resolve, reject) => {
      login(userName.trim(), password, smsCode, code).then(data => {
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        commit('SET_USER_INFO', data.userInfo)
        setCookieByKey('YY_USER_INFO', data.userInfo)
        resolve(data.userInfo)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      resetRouter()
      removeToken()
      removeCookieByKey('YY_USER_INFO')
      commit('RESET_STATE')
      resolve()
    })
  },

  // 退出登录
  resetToken({ commit }) {
    return new Promise((resolve, reject) => {
      logout().then(data => {
        if (data && data.result) {
          removeToken()
          resetRouter()
          removeCookieByKey('YY_USER_INFO')
          removeCookieByKey('YY_USER_COMPANY')
          removePlatform()
          commit('RESET_STATE')
          store.dispatch('app/togglePlatform', '')
          store.dispatch('permission/setRoutes')
          store.dispatch('tagsView/delAllVisitedViews')
          resolve()
        }
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

