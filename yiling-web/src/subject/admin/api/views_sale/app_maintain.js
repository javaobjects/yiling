import request from '@/subject/admin/utils/request';
// app管理
// app管理 新增 修改
export function bannerSave(
  activityLinks, //活动详情超链接
  bannerCondition, //	使用场景：1-以岭内部机构 2-
  bannerStatus, // 显示状态：1-启用 2-停用
	id, //修改时需要的banner的id
  pic, //banner图片地址
  sort, //排序,
  startTime, //有效起始时间	
  stopTime, //有效结束时间
  title, // banner标题
  usageScenario //使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner
) {
  return request({
    url: '/salesAssistant/api/v1/banner/save',
    method: 'post',
    data: {
      activityLinks, 
      bannerCondition, 
      bannerStatus,
      id,
      pic,
      sort,
      startTime,
      stopTime,
      title,
      usageScenario
    }
  });
}
// 查询以岭所属企业
export function listYiLingEnterprise(){
  return request({
    url: '/salesAssistant/api/v1/banner/listYiLingEnterprise',
    method: 'get',
    params: {}
  })
}
// app版本列表
export function bannerPageList(
  bannerCondition, //使用场景：0-所有 1-以岭内部机构 2-非以岭机构
	bannerStatus, //发布状态：状态：0-全部 1-启用 2-停用
	createEndTime, //创建截止时间
	createStartTime, //创建起始时间
  current, //第几页，默认：1
  size, //每页记录数，默认：10
	title, //banner标题
  usageScenario, //使用位置：0-所有 1-B2B移动端主Banner 2-B2B移动端副Banner
	useEndTime, //投放截止时间
	useStartTime //投放起始时间
){
  return request({
    url: '/salesAssistant/api/v1/banner/pageList',
    method: 'post',
    data: {
      bannerCondition,
      bannerStatus,
      createEndTime, 
      createStartTime, 
      current,
      size,
      title,
      usageScenario,
      useEndTime,
      useStartTime
    }
  })
}
// 启用 /禁用 
export function editStatus(
  bannerStatus, //状态：1-启用 2-停用
	id //id
){
  return request({
    url: '/salesAssistant/api/v1/banner/editStatus',
    method: 'post',
    data: {
      bannerStatus,
      id
    }
  })
}
// 排序
export function editWeight(
	id, //id
  sort //序号
){
  return request({
    url: '/salesAssistant/api/v1/banner/editWeight',
    method: 'post',
    data: {
      id,
      sort
    }
  })
}
// 删除
export function bannerDelete(
	id //id
){
  return request({
    url: '/salesAssistant/api/v1/banner/delete',
    method: 'post',
    data: {
      id
    }
  })
}
// 通过id查询banner
export function getById(
	id //id
){
  return request({
    url: '/salesAssistant/api/v1/banner/getById',
    method: 'get',
    params: {
      id
    }
  })
}
// 上架下架
export function release(
  id, //banner的id
	releaseType //操作:0 发布, 1 下架
){
  return request({
    url: '/salesAssistant/api/v1/banner/release',
    method: 'post',
    data: {
      id,
      releaseType
    }
  })
}
// 版本发布 
export function pageList(
  appType,
  current,
  size
){
  return request({
    url: '/salesAssistant/api/v1/version/pageList',
    method: 'post',
    data: {
      appType,
      current,
      size
    }
  })
}
// 选择app-运营后台
export function listAppInfo(){
  return request({
    url: '/salesAssistant/api/v1/version/listAppInfo',
    method: 'get',
    params: {}
  })
}
//  app维护 新增和编辑版本
export function appSave(
  appId, //应用ID
	appType, //	App类型：1-android 2-ios
	appUrl, //APP地址
	channelCode, //	渠道号
	description, //	版本说明
	forceUpgradeFlag, //是否强制升级：0-否 1-是
	id,
	name, //版本名称
	packageMd5, //安装包MD5
	packageSize, //安装包大小(KB)
	packageUrl, //安装包下载地址
	upgradeTips, //升级提示语
	version //版本号
){
  return request({
    url: '/salesAssistant/api/v1/version/save',
    method: 'post',
    data: {
      appId,
      appType,
      appUrl,
      channelCode,
      description,
      forceUpgradeFlag,
      id,
      name,
      packageMd5,
      packageSize,
      packageUrl,
      upgradeTips,
      version
    }
  })
}

//开屏位
export function queryListPage(
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size
){
  return request({
    url: '/salesAssistant/api/v1/openPosition/queryListPage',
    method: 'post',
    data: {
      current,
      size
    }
  })
}
//保存/编辑开屏位
export function saveOpenPosition(
  //ID（新增不传入，编辑必传）
  id,
  //标题
  title,
  //配置链接
  link,
  //发布状态：1-暂不发布 2-立即发布
  status,
  //图片
  picture
){
  return request({
    url: '/salesAssistant/api/v1/openPosition/saveOpenPosition',
    method: 'post',
    data: {
      id,
      title,
      link,
      status,
      picture
    }
  })
}
//详情
export function homeById(
  id
){
  return request({
    url: '/salesAssistant/api/v1/openPosition/getById',
    method: 'get',
    params: {
      id
    }
  })
}
//更新状态
export function updateStatus(
  //ID（新增不传入，编辑必传）
  id,
  //发布状态：1-暂不发布 2-立即发布
  status
){
  return request({
    url: '/salesAssistant/api/v1/openPosition/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}
//删除开屏位
export function deleteOpenPosition(
  id
){
  return request({
    url: '/salesAssistant/api/v1/openPosition/deleteOpenPosition',
    method: 'get',
    params: {
      id
    }
  })
}