// 企业账号 数据接口
import request from '@/subject/admin/utils/request'
// 获取企业标签选项列表
export function options(){
  return request({
    url: '/dataCenter/api/v1/enterprise/tag/options',
    method: 'get',
    params: {}
  })
}
// 获取企业分页列表
export function pageList(
  authStatus, //认证状态：0-全部 1-未认证 2-认证通过 3-认证不通过
	cityCode, //所属城市编码
	// contactor, //	联系人
  tagIds, //标签ID列表
	contactorPhone, //联系人电话
	current, //第几页，默认：1
	id, //企业ID
	licenseNumber, //执业许可证号/社会信用统一代码
	name, //企业名称
	provinceCode, //所属省份编码
	regionCode, //所属区域编码
	size, //每页记录数，默认：10
	status, //状态：0-全部 1-启用 2-停用
	type, //类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
  erpSyncLevel //ERP对接级别：0-未对接 1-基础对接 2-订单对接 3-发货单对接
){
  return request({
    url: '/dataCenter/api/v1/enterprise/pageList',
    method: 'post',
    data: {
      authStatus,
      cityCode,
      // contactor,
      tagIds,
      contactorPhone,
      current,
      id,
      licenseNumber,
      name,
      provinceCode,
      regionCode,
      size,
      status,
      type,
      erpSyncLevel
    }
  })
}
// 获取企业数量统计
export function quantityStatistics(){
  return request({
    url: '/dataCenter/api/v1/enterprise/quantityStatistics',
    method: 'get',
    params: {}
  })
}
// 倒入企业数据
export function importData(
  currentUserId, 
  ip
){
  return request({
    url: '/dataCenter/api/v1/enterprise/importData',
    method: 'post',
    data: {
      currentUserId, 
      ip
    }
    
  })
}
// 获取企业详情
export function businessDetails(id){
  return request({
    url: '/dataCenter/api/v1/enterprise/get',
    method: 'get',
    params: {
      id //企业ID
    }
  })
}
// 获取单个企业标签列表
export function listByEid(eid){
  return request({
    url: '/dataCenter/api/v1/enterprise/tag/listByEid',
    method: 'get',
    params: {
      eid //企业ID
    }
  })
}
// 更新企业状态
export function updateStatus(
  id, //企业id
	status //1-启用 2-停用
){
  return request({
    url: '/dataCenter/api/v1/enterprise/updateStatus',
    method: 'post',
    data: {
      id, 
      status
    }
  })
}
// 修改企业信息
export function enterpriseUpdate(
  address, //详细地址
	cityCode, //	所属城市编码
	contactor, //联系人
	contactorPhone, //联系人电话
	id, //企业id
	licenseNumber, //执业许可证号/社会信用统一代码
	name, //企业名称
	provinceCode, //所属省份编码
	regionCode //所属区域编码
){
  return request({
    url: '/dataCenter/api/v1/enterprise/update',
    method: 'post',
    data: {
      address,
      cityCode,
      contactor,
      contactorPhone,
      id,
      licenseNumber,
      name,
      provinceCode,
      regionCode
    }
  })
}
// 修改企业开通平台
export function openPlatform(
  channelCode,
	eid,
	platformCodeList,
  hmcType // C端药+险类型（开通C端药+险时必填
){
  return request({
    url: '/dataCenter/api/v1/enterprise/openPlatform',
    method: 'post',
    data: {
      channelCode,
      eid,
      platformCodeList,
      hmcType
    }
  })
}
// 标签管理
// 查询标签列表
export function queryTagsListPage(
  current, //第几页，默认：1
	name, //名称
	size //每页记录数，默认：10
){
  return request({
    url: '/dataCenter/api/v1/enterprise/tag/queryTagsListPage',
    method: 'post',
    data: {
      current, 
      name,
      size 
    }
  })
}
// 新增标签
export function createTags(
  description, //描述
	name, //	名称
	type //	类型：1-手动标签 2-自动标签
){
  return request({
    url: '/dataCenter/api/v1/enterprise/tag/createTags',
    method: 'post',
    data: {
      description, 
      name,
      type
    }
  })
}
// 修改标签
export function updateTags(
  description, //描述
  id, 
	name, //	名称
	type //	类型：1-手动标签 2-自动标签
){
  return request({
    url: '/dataCenter/api/v1/enterprise/tag/updateTags',
    method: 'post',
    data: {
      description, 
      id,
      name,
      type
    }
  })
}
//删除标签
export function batchDeleteTags(
  tagsIdList //id数组
){
  return request({
    url: '/dataCenter/api/v1/enterprise/tag/batchDeleteTags',
    method: 'post',
    data: {
      tagsIdList
    }
  })
}
// 保存单个企业标签信息 使标签与企业建立关联关系
export function saveEnterpriseTags(eid,tagIds){
  return request({
    url: '/dataCenter/api/v1/enterprise/tag/saveEnterpriseTags',
    method: 'post',
    data: {
      eid, //企业ID
      tagIds //标签id数组
    }
  })
}
//账号信息查询

// 获取企业下账号列表
export function getCompanyUserList(
  current,
  size,
  // 企业名称
  ename = '',
  // 企业类型
  etype,
  // 手机号
  mobile = '',
  // 姓名
  name = '',
  // 状态
  status
) {
  return request({
    url: '/dataCenter/api/v1/staff/pageList',
    method: 'post',
    data: {
      current,
      size,
      ename,
      etype,
      mobile,
      name,
      status
    }
  })
}
// 获取员工账号详情
export function getCompanyUserDetail(id = '') {
  return request({
    url: '/dataCenter/api/v1/staff/get',
    method: 'get',
    params: {
      id
    }
  })
}
// 修改账号状态
export function updateCompanyUser(
  id,
  // 状态
  status
) {
  return request({
    url: '/dataCenter/api/v1/staff/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}
// 修改企业下账号姓名
export function updateCompanyUserName(id, name = '') {
  return request({
    url: '/dataCenter/api/v1/staff/updateName',
    method: 'post',
    data: {
      id,
      name
    }
  })
}
// 修改企业下账号手机号
export function updateCompanyTel(id, mobile = '') {
  return request({
    url: '/dataCenter/api/v1/staff/updateMobile',
    method: 'post',
    data: {
      id,
      mobile
    }
  })
}
// 修改企业员工账号状态
export function unbindCompanyUser(userId, eid, status) {
  return request({
    url: '/dataCenter/api/v1/staff/updateEmployeeStatus',
    method: 'post',
    data: {
      userId,
      eid,
      status
    }
  })
}
// 根据手机号获取用户信息
export function getByMobile(mobile) {
  return request({
    url: '/dataCenter/api/v1/staff/getByMobile',
    method: 'get',
    params: {
      mobile
    }
  })
}
// 修改管理员账号
export function updateManagerAccount(
  eid, //企业ID
	name, //姓名
	newMobile, //新手机号
	userId //用户ID
) {
  return request({
    url: '/dataCenter/api/v1/enterprise/updateManagerAccount',
    method: 'post',
    data: {
      eid,
      name,
      newMobile,
      userId
    }
  })
}
// 更新企业类型
export function updateType(
  eid, //企业ID
  type
) {
  return request({
    url: '/dataCenter/api/v1/enterprise/updateType',
    method: 'post',
    data: {
      eid,
      type
    }
  })
}
//更新企业渠道 
export function updateChannel(
  channelId,
  eid //企业ID
) {
  return request({
    url: '/dataCenter/api/v1/enterprise/updateChannel',
    method: 'post',
    data: {
      channelId,
      eid
    }
  })
}
// 更新企业HMC业务类型
export function updateHmcType(
  hmcType, //HMC业务类型：1-药+险销售 2-药+险销售与药品兑付
  eid //企业ID
) {
  return request({
    url: '/dataCenter/api/v1/enterprise/updateHmcType',
    method: 'post',
    data: {
      hmcType,
      eid
    }
  })
}
// 重置密码
export function resetPassword( mobile ) {
  return request({
    url: '/dataCenter/api/v1/staff/resetPassword',
    method: 'get',
    params: {
      mobile
    }
  })
}

//下载推广查看
//查询企业推广下载记录分页列表
export function queryListPage(
  //推广方名称
  promoterName,
  //推广方ID
  promoterId,
  //开始下载时间
  startDownloadTime,
  //结束下载时间
  endDownloadTime,
  //推广方省份编码
  provinceCode,
  //推广方城市编码
  cityCode,
  //推广方区域编码
  regionCode,
  current,
  size
) {
  return request({
    url: '/dataCenter/api/v1/promotionDownloadRecord/queryListPage',
    method: 'post',
    data: {
      promoterName,
      promoterId,
      startDownloadTime,
      endDownloadTime,
      provinceCode,
      cityCode,
      regionCode,
      current,
      size
    }
  })
}