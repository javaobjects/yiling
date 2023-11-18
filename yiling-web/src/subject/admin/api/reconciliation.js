// 对账管理
import request from '@/subject/admin/utils/request';
// 企业返利入账申请
export function queryApplyPageList(
  code, //申请单号
	current, //默认第几页
	name,//企业名称
	easCode,//企业编码
	// queryType,//查询类型（默认商务） 1-商务 2-财务
	size,//每页记录数
	status//申请单状态 0-全部 1-待审核 2-已入账 3-驳回
){
 return request({
  url: '/pop/api/v1/rebate/queryApplyPageList',
  method: 'post',
  data: {
    code, //申请单号
    current, //默认第几页
    name,//企业名称
    easCode,//企业编码
    // queryType,//查询类型（默认商务） 1-商务 2-财务
    size,//每页记录数
    status
  }
 })
}
// 企业返利入账申请 查看
export function queryRebateApplyPageList
(applyId,current,size){
  return request({
    url: '/pop/api/v1/rebate/queryRebateApplyPageList',
    method: 'post',
    data: {
      applyId,current,size
    }
  })
}
// 企业返利入账申请 协议订单商品明细
export function queryRebateApplyOrderDetailPageList(applyId,current,size){
  return request({
    url: '/pop/api/v1/rebate/queryRebateApplyOrderDetailPageList',
    method: 'post',
    data: {
      applyId,current,size
    }
  })
}
// 企业返利 对账 列表
export function queryFinancialRebateEntPageList
(
  current,//第几页，默认：1
	customerName,//企业名称（全模糊查询）
	easCode,//企业编码
	licenseNumber,//执业许可证号/社会信用统一代码
	size//每页记录数，默认：10
){
  return request({
    url: '/pop/api/v1/rebate/queryFinancialRebateEntPageList',
    method: 'post',
    data: {
      current,customerName,easCode,licenseNumber,size
    }
  })
}
// 企业返利 对账 以兑付金额 数据
export function queryFinanceApplyListPageList(
  current,//第几页，默认：1
	easCode,//企业编码
	eid, //id
	size//每页记录数，默认：10
){
  return request({
    url: '/pop/api/v1/rebate/queryFinanceApplyListPageList',
    method: 'post',
    data: {current,easCode,eid,size}
  })
}
// 企业返利 对账  已使用金额 详情
export function queryFinanceUseListPageList(
  current,//第几页，默认：1
	easCode,//企业编码
	eid, //id
	size//每页记录数，默认：10
){
  return request({
    url: '/pop/api/v1/rebate/queryFinanceUseListPageList',
    method: 'post',
    data: {current,easCode,eid,size}
  })
}
// 企业返利 对账  已申请入账返利信息-协议订单商品明细
export function queryRebateOrderDetailPageList(
  account,//	企业账号
	agreementId,//协议id
	cashStatus, //兑付状态 1-未兑付 2-已经兑付，，仅在查询以兑付订单明细时该字段传成2
	conditionStatus//1-可对账 2-不可对账，仅在查询待兑付订单明细时需要传该字段
){
  return request({
    url: '/pop/api/v1/rebate/queryRebateOrderDetailPageList',
    method: 'post',
    data: { account,agreementId,cashStatus,conditionStatus }
  })
}
