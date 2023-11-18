const getters = {
  // 是否配置了首页
  hasDashboard: state => state.permission.hasDashboard,
  accessRoutes: state => state.permission.routes,
  sidebar: state => state.app.sidebar,
  sidebarWidth: state => state.app.sidebar.opened ? '182px' : '54px',
  device: state => state.app.device,
  dict: state => state.app.dict,
  // 顶部菜单栏
  topList: state => state.app.topList,
  token: state => state.user.token,
  platform: state => state.app.platform,
  userInfo: state => state.user.userInfo
}
export default getters
