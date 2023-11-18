import request from '@/subject/admin/utils/request'
// 企业审核
// 获取企业认证审核分页列表
export function pageList(
  //审核状态：1-待审核 2-审核通过 3-审核不通过	
  authStatus, 
  //审核类型：1-首次认证 2-资质更新 3-驳回后再次认证
	authType, 
  //所属城市编码
	cityCode, 
   //第几页，默认：1
	current,
  //申请结束时间
	endCreateTime, 
  //执业许可证号/社会信用统一代码
	licenseNumber, 
  //企业名称
	name, 
  //所属省份编码
	provinceCode, 
  //所属区域编码
	regionCode, 
  //每页记录数，默认：10
	size, 
  //数据来源：1-B2B 2-销售助手
	source, 
  //申请开始时间
	startCreateTime, 
  //类型：0-全部 1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
	type 
) {
  return request({
    url: '/dataCenter/api/v1/enterpriseAuth/pageList',
    method: 'post',
    data: {
      authStatus, 
      authType, 
      cityCode, 
      current, 
      endCreateTime, 
      licenseNumber,
      name, 
      provinceCode,
      regionCode,
      size,
      source,
      startCreateTime,
      type 
    }
  })
}
// 获取企业审核详情（首次认证）
export function enterpriseAuthGet(
  id
){
  return request({
    url: '/dataCenter/api/v1/enterpriseAuth/get',
    method: 'get',
    params: {
      id
    }
  })
}
// 获取企业审核详情（资质更新）
export function getCertificateUpdate(
  id
){
  return request({
    url: '/dataCenter/api/v1/enterpriseAuth/getCertificateUpdate',
    method: 'get',
    params: {
      id
    }
  })
}
// 审核
export function updateAuth(
  authRejectReason,
	authStatus,
	id
){
  return request({
    url: '/dataCenter/api/v1/enterpriseAuth/updateAuth',
    method: 'post',
    data: {
      authRejectReason,
      authStatus,
      id
    }
  })
}

// 企业收款账户列表
// 查询收款账户列表
export function queryReceiptAccountPageList(
  //第几页，默认：1
  current, 
  //社会信用统一代码
	licenseNumber, 
  //最大提交时间
	maxDate, 
  //最小提交时间
	minDate, 
  //	企业名称
	name, 
   //每页记录数，默认：10
	size,
   //账户状态
	status,
  //类型
	type 
){
  return request({
    url: '/dataCenter/api/v1/receiptAccount/queryReceiptAccountPageList',
    method: 'post',
    data: {
      current, 
      licenseNumber,
      maxDate,
      minDate,
      name,
      size,
      status,
      type
    }
  })
}
// 查询收款账户
export function queryReceiptAccount(
  receiptAccountId
){
  return request({
    url: '/dataCenter/api/v1/receiptAccount/queryReceiptAccount',
    method: 'post',
    data: {
      receiptAccountId
    }
  })
}
// 企业收款账户审核
export function auditReceiptAccount(
  auditRemark, //审核描述
	id, //企业收款账户id
	status //账户状态
){
  return request({
    url: '/dataCenter/api/v1/receiptAccount/auditReceiptAccount',
    method: 'post',
    data: {
      auditRemark,
      id,
      status
    }
  })
}

// 账号注销审核列表
export function cancellationPageList(
  // 第几页，默认：1
  current, 
  // 结束申请注销时间
	endApplyTime,
  // 手机号
	mobile,
  // 姓名
	name,
  // 每页记录数，默认：10
	size,
  // 开始申请注销时间
	startApplyTime,
  // 注销状态：1-待注销 2-已注销 3-已撤销
	status, 
  // 用户ID
	userId
) {
  return request({
    url: '/dataCenter/api/v1/userDeregisterAuth/queryPageList',
    method: 'post',
    data: {
      current, 
      endApplyTime,
      mobile,
      name,
      size,
      startApplyTime,
      status, 
      userId 
    }
  })
}

// 账号注销获取审核详情
export function userAuthDetail(
  // ID
  id
) {
  return request({
    url: '/dataCenter/api/v1/userDeregisterAuth/getById',
    method: 'get',
    params: {
      id
    }
  })
}

// 账号注销确认审核
export function userAuthUpdateAuth(
  // ID
  id, 
  // 注销状态：1-待注销 2-已注销 3-已撤销	
	status
) {
  return request({
    url: '/dataCenter/api/v1/userDeregisterAuth/updateAuth',
    method: 'post',
    data: {
      id, 
      status
    }
  })
}
