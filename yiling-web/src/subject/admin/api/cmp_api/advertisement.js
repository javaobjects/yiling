// 广告管理
import request from '@/subject/admin/utils/request'
// 广告信息新增和修改
export function save(
  id,
  //图片地址
	pic,
  //投放位置:1-C端用户侧首页 2-C端用户侧我的	
	position,
  //显示顺序
	sort, 
  //有效起始时间
	startTime,
  //有效截止时间
	stopTime,
  //标题
	title,
  //链接地址
	url,
  //跳转类型 1-h5跳转，2-小程序内部跳转
  redirectType
){
  return request({
    url: '/hmc/api/v1/advertisement/save',
    method: 'post',
    data: {
      id,
      pic,
      position,
      sort,
      startTime,
      stopTime,
      title,
      url,
      redirectType
    }
  })
}
// 广告信息分页查询
export function pageList(
  current, //第几页，默认：1
	position, //	投放位置:1-C端用户侧首页 2-C端用户侧我的	
	size //每页记录数，默认：10
) {
  return request({
    url: '/hmc/api/v1/advertisement/pageList',
    method: 'post',
    data: {
      current,
      position,
      size
    }
  })
}
// 通过id查询广告信息
export function queryById(
  id
){
  return request({
    url: '/hmc/api/v1/advertisement/queryById',
    method: 'get',
    params: {
      id
    }
  })
}
// 广告删除
export function deletes(
  id
) {
  return request({
    url: '/hmc/api/v1/advertisement/delete',
    method: 'post',
    data: {
     id
    }
  })
}
// 版本发布 
export function versionPageList(
  appType,
  current,
  size
){
  return request({
    url: '/hmc/api/v1/version/pageList',
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
    url: '/hmc/api/v1/version/listAppInfo',
    method: 'get',
    params: {}
  })
}
//  app维护 新增和编辑版本
export function appSave(
  //应用ID
  appId,
  //App类型：1-android 2-ios
	appType,
  //APP地址
  appUrl,
  //渠道号
	channelCode,
  //版本说明
	description,
  //是否强制升级：0-否 1-是
	forceUpgradeFlag,
	id,
  //版本名称
	name,
  //安装包MD5
	packageMd5,
  //安装包大小(KB)
	packageSize,
  //安装包下载地址
	packageUrl,
  //升级提示语
	upgradeTips,
  //版本号
	version 
){
  return request({
    url: '/hmc/api/v1/version/save',
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
