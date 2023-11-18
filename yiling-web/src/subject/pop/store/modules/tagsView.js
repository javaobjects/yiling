const state = {
  cachedViews: []
}

const mutations = {
  ADD_CACHED_VIEW: (state, view) => {
    if (state.cachedViews.includes(view.name)) return
    if (view.hidden === true) {
      if (typeof view.meta.noCache !== 'undefined' && view.meta.noCache === false) {
        state.cachedViews.push(view.name)
      }
    } else {
      if (!view.meta.noCache) {
        state.cachedViews.push(view.name)
      }
    }
  }
}

const actions = {
  addCachedView({ commit }, view) {
    commit('ADD_CACHED_VIEW', view)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
