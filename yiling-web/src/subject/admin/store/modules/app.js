import Cookies from 'js-cookie'
import { getDictList } from '@/subject/admin/api/common'
import { getUserNavRouter } from '@/subject/admin/api/user'
import { setPlatform, getPlatform } from '@/subject/admin/utils/auth'

const state = {
  sidebar: {
    opened: true,
    withoutAnimation: false
  },
  device: 'desktop',
  platform: getPlatform(),
  dict: [],
  // 顶部菜单
  topList: []
}

const mutations = {
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0)
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  TOGGLE_PLATFORM: (state, platform) => {
    setPlatform(platform)
    state.platform = platform + ''
  },
  SET_DICT: (state, dict) => {
    state.dict = [].concat(dict)
  },
  SET_TOP_LIST: (state, dict) => {
    state.topList = [].concat(dict)
  }
}

const actions = {
  toggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  toggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  togglePlatform({ commit }, platform) {
    commit('TOGGLE_PLATFORM', platform)
  },
  getDict({ commit }) {
    return new Promise((resolve, reject) => {
      getDictList().then(data => {
        commit('SET_DICT', data.list)
        resolve(data.list)
      }).catch(error => {
        reject(error)
      })
    })
  },
  getPlatform({ commit }) {
    return new Promise((resolve, reject) => {
      getUserNavRouter().then(data => {
        if (data && data.list && data.list.length) {
          let list = data.list
          commit('TOGGLE_PLATFORM', list[0].id)
          resolve(list[0].id)
        }
      }).catch(error => {
        reject(error)
      })
    })
  },
  getTopList({ commit }, token) {
    return new Promise((resolve, reject) => {
      getUserNavRouter(token).then(data => {
        if (data && data.list && data.list.length) {
          let list = data.list
          commit('SET_TOP_LIST', list)
          resolve(list)
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
