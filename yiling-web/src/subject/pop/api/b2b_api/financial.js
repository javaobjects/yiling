import request from '@/subject/pop/utils/request'
// 财务管理

// 企业收款账户
export function queryReceiptAccount() {
  return request({
    url: '/admin/b2b/api/v1/receiptAccount/queryReceiptAccount',
    method: 'post',
    data: {}
  })
}
// 新增或修改企业收款账户---form方式请求
export function saveOrUpdateReceiptAccount(
  obj
  // account, //银行账户	
  // bankCode,//总行编码
  // bankId, //总行id
  // bankName, //开户行名称
  // branchBankId, //支行id
  // branchCode, //支行编码
  // cityCode, //市code
  // cityName, //市name
  // licenceFile, //其他证照
  // name, //企业收款账户名称
  // provinceCode, //省份code
  // provinceName, //省份name
  // receiptAccountId, //企业收款账户信息id---修改账户信息时必传
  // subBankName, //支行名称

  // currentEid, //id
  // accountOpeningPermitUrl //开户许可证
) {
  return request({
    url: '/admin/b2b/api/v1/receiptAccount/saveOrUpdateReceiptAccount',
    method: 'post',
    data: {
      ...obj
      // account,
      // bankCode,
      // bankId,
      // bankName,
      // branchBankId,
      // branchCode,
      // cityCode,
      // cityName,
      // licenceFile,
      // name,
      // provinceCode,
      // provinceName,
      // receiptAccountId,
      // subBankName,
      
    }
  })
}
// 查询银行列表
export function queryBankPageList(
  current, //	第几页，默认：1
	name, //名称(模糊）
	size, //每页记录数，默认：10
  type //银行类型 1-总行 2-支行
) {
  return request({
    url: '/admin/b2b/api/v1/receiptAccount/queryBankPageList',
    method: 'post',
    data: {
      current, 
      name, 
      size, 
      type
    }
  })
}
// 结算单对账
// 结算单---查询订单明细列表
export function queryOrderDetailPageList(
  current, //	第几页，默认：1
	orderId, //订单id
	settlementId, //订单id
	size //每页记录数，默认：10
) {
  return request({
    url: '/admin/b2b/api/v1/settlement/queryOrderDetailPageList',
    method: 'post',
    data: {
      current,
      orderId,
      settlementId,
      size
    }
  })
}
// 查询结算单明细列表
export function querySettlementDetailPageList(
  current, //第几页，默认：1
	settlementId, //结算单id
	size //每页记录数，默认：10
) {
  return request({
    url: '/admin/b2b/api/v1/settlement/querySettlementDetailPageList',
    method: 'post',
    data: {
      current,
      settlementId,
      size
    }
  })
}
// 订单对账---查询订单明细列表
export function querySettlementOrderDetailPageList(
  orderId
) {
  return request({
    url: '/admin/b2b/api/v1/settlement/querySettlementOrderDetailPageList',
    method: 'post',
    data: {
      orderId
    }
  })
}
// 查询订单对账列表
export function querySettlementOrderPageList(
  //货款结算状态
  goodsStatus,
	maxCreateTime,
	minCreateTime,
  //订单号
	orderNo,
  //促销结算状态
	saleStatus,
  //预售违约结算状态
  presaleDefaultStatus,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size
) {
  return request({
    url: '/admin/b2b/api/v1/settlement/querySettlementOrderPageList',
    method: 'post',
    data: {
      goodsStatus,
      maxCreateTime,
      minCreateTime,
      orderNo,
      saleStatus,
      presaleDefaultStatus,
      current,
      size
    }
  })
}
// 查询结算单列表
export function querySettlementPageList(
  code, //结算单号
	current, //第几页，默认：1
	maxTime, //	开始生成结算单最大时间
	minTime, //开始生成结算单最小时间
	size, //每页记录数，默认：10
	status, //结算单状态 1-待结算 2-银行处理中 3-已结算 4-银行处理失败
	type //结算单类型 1-货款结算单 2-促销结算单
) {
  return request({
    url: '/admin/b2b/api/v1/settlement/querySettlementPageList',
    method: 'post',
    data: {
      code,
      current,
      maxTime,
      minTime,
      size,
      status,
      type
    }
  })
}