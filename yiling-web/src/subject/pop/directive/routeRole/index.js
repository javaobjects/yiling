import routeRole from './routeRole'

const install = function(Vue) {
  Vue.directive('routeRole', routeRole)
}

if (window.Vue) {
  window['routeRole'] = routeRole
  Vue.use(install);
}

routeRole.install = install
export default routeRole
