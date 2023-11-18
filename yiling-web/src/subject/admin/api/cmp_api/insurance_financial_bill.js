import request from '@/subject/admin/utils/request'

// 财务账单-账务中心-保司结账分页列表
export function getInsuranceSettlement(
  current,
  size,
  insuranceCompanyName, // 保险提供服务商名称
  startTime,
  stopTime
) {
  return request({
    url: '/hmc/api/v1/settlement/insurance/pageList',
    method: 'post',
    data: {
      current,
      size,
      insuranceCompanyName,
      startTime,
      stopTime
    }
  });
}
// 财务账单-账务中心-保司结账分页列表-详情
export function getInsuranceSettlementDetail(
  id
) {
  return request({
    url: '/hmc/api/v1/settlement/insurance/queryDetail',
    method: 'get',
    params: {
      id
    }
  });
}
// 财务账单-账务中心-以领给商家药品对账
export function getEnterpriseSettlement(
  current,
  size,
  ename // 药店名称
) {
  return request({
    url: '/hmc/api/v1/settlement/enterprise/pageSettlement',
    method: 'post',
    data: {
      current,
      size,
      ename
    }
  });
}
// 财务账单-账务中心-以领给商家药品对账-详情-终端结算账单
export function getEnterpriseSettlementDetail(
  current,
  size,
  eid, // 药品服务终端id
  terminalSettleStatus, // 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
  goodsName, // 商品名称
  startTime, // 创建开始时间
  stopTime, // 创建截止时间
  payStartTime, // 结算起始时间
  payStopTime, // 结算结束时间
  insuranceSettlementStatus // 保司结算状态:1-待结算/2-已结算/3-无需结算失效单/4-预付款抵扣已结
) {
  return request({
    url: '/hmc/api/v1/settlement/enterprise/pageList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      terminalSettleStatus,
      goodsName,
      startTime,
      stopTime,
      payStartTime,
      payStopTime,
      insuranceSettlementStatus
    }
  });
}
// 理赔对账 列表页
export function querySyncInsuranceOrder(
  // 保险提供服务商id
  insuranceCompanyId,
  // 开方状态 0-全部 1-已开，2-未开
  prescriptionStatus,
  current,
  size
) {
  return request({
    url: '/hmc/api/v1/settlement/insurance/querySyncInsuranceOrder',
    method: 'post',
    data: {
      insuranceCompanyId,
      prescriptionStatus,
      current,
      size
    }
  })
}
// 同步订单数据到保司
export function syncOrder(
  // 保司id
  id
) {
  return request({
    url: '/hmc/api/v1/settlement/insurance/syncOrder',
    method: 'post',
    data: {
      id
    }
  })
}
// 获取同步订单数据到保司的结果
export function getSyncOrderResult(
  // 保司id
  id
) {
  return request({
    url: '/hmc/api/v1/settlement/insurance/getSyncOrderResult',
    method: 'post',
    data: {
      id
    },
    // 接口返回特殊处理
    noErr: true
  })
}