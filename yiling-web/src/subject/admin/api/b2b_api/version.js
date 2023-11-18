// 版本管理接口
import request from '@/subject/admin/utils/request'

// 分页列表查询版本-运营后台
export function pageList(
  appType, //App类型：1-android 2-ios
	current, //第几页，默认：1
	size //每页记录数，默认：10
) {
  return request({
    url: '/b2b/api/v1/version/pageList',
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
    url: '/b2b/api/v1/version/listAppInfo',
    method: 'get',
    params: {}
  })
}
// 新增和编辑版本
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
    url: '/b2b/api/v1/version/save',
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