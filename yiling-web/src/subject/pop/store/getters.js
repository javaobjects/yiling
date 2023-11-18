const getters = {
  // 是否配置了首页
  hasDashboard: state => state.permission.hasDashboard,
  accessRoutes: state => state.permission.routes,
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  // 全局字典
  dict: state => state.app.dict,
  // 顶部菜单栏
  topList: state => state.app.topList,
  token: state => state.user.token,
  // 平台
  platform: state => state.app.platform,
  // 用户信息
  userInfo: state => state.user.userInfo,
  // 用户绑定公司
  userCompany: state => state.user.company,
  // 当前企业信息
  // adminFlag	是否企业管理员	boolean
  // channelId	渠道类型：1-工业 2-工业直属企业 3-一级商 4-二级商 5-KA用户	integer(int64)
  // id	企业ID	integer(int64)
  // name	企业名称	string
  // type	企业类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所	integer(int32)
  // yilingAdminFlag	是否是以岭本部企业管理员	boolean
  // yilingFlag	是否是以岭本部
  currentEnterpriseInfo: state => state.user.userInfo.currentEnterpriseInfo
}
export default getters
