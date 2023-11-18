import { login, smsLogin, changeMyCompany, logout } from '@/subject/pop/api/user'
import { getToken, setToken, removeToken, setCookieByKey, getCookieByKey, removeCookieByKey, removePlatform, getCurrentUser, keys } from '@/subject/pop/utils/auth'
import { resetRouter } from '@/subject/pop/router'
import store from '@/subject/pop/store'
import _ from 'lodash'

const getDefaultState = () => {
  return {
    token: getToken() || '',
    userInfo: getCurrentUser() || {},
    company: getCookieByKey(keys.companyKey)
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
  },
  SET_USER_COMPANY: (state, company) => {
    state.company = company
  }
}

const actions = {
  // 密码登录
  login({ commit }, userInfo) {
    const { userName, password, smsCode, code } = userInfo
    return new Promise((resolve, reject) => {
      login(userName.trim(), password, smsCode, code).then(data => {
        if (data && data.code && data.message) {
          reject(data)
        } else {
          commit('SET_TOKEN', data.token)
          // setToken(data.token)
          let user = _.cloneDeep(Object.assign(data.userInfo, {
            currentEnterpriseInfo: data.currentEnterpriseInfo || {}
          }))
          commit('SET_USER_INFO', user)
          resolve(user)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 密码登录
  smsLogin({ commit }, userInfo) {
    const { userName, smsCode } = userInfo
    return new Promise((resolve, reject) => {
      smsLogin(userName.trim(), smsCode).then(data => {
        if (data && data.code && data.message) {
          reject(data)
        } else {
          commit('SET_TOKEN', data.token)
          // setToken(data.token)
          let user = _.cloneDeep(Object.assign(data.userInfo, {
            currentEnterpriseInfo: data.currentEnterpriseInfo || {}
          }))
          commit('SET_USER_INFO', user)
          resolve(user)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },
  // 切换企业
  changeCompany({ commit }, {id, token}) {
    return new Promise((resolve, reject) => {
      changeMyCompany(id, token).then(data => {
        if (data && data.token) {
          commit('SET_USER_COMPANY', id)
          setCookieByKey(keys.companyKey, id)
          commit('SET_TOKEN', data.token)
          setToken(data.token)
          let user = _.cloneDeep(Object.assign(data.userInfo, {
            currentEnterpriseInfo: data.currentEnterpriseInfo || {}
          }))
          commit('SET_USER_INFO', user)
          setCookieByKey(keys.userInfoKey, user)
          resolve(true)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 退出
  resetToken({ commit }) {
    return new Promise((resolve, reject) => {
      logout().then(data => {
        if (data && data.result) {
          removeToken()
          resetRouter()
          removeCookieByKey(keys.userInfoKey)
          removeCookieByKey(keys.companyKey)
          removePlatform()
          commit('RESET_STATE')
          store.dispatch('app/togglePlatform', '')
          store.dispatch('permission/setRoutes')
          resolve(true)
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

