import request from '@/subject/admin/utils/request'

// 保险订单管理-保险销售单-分页列表-按保单查
export function insuranceRecordList(
  current,
  size,
  insuranceName, // 保险名称
  insuranceCompanyId, // 保险服务商ID
  sourceType, // 来源类型 0 线上 1 线下
  terminalName, // 保单来源终端
  policyNo, // 保司保单号
  policyStatus, // 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
  billType, // 定额方案类型
  issueName, // 被保人姓名
  issuePhone, // 被保人电话
  holderName, // 投保人姓名
  sellerName, // 销售员姓名
  startProposalTime, // 投保日期起
  endProposalTime // 投保日期止
) {
  return request({
    url: '/hmc/api/v1/insurance/record/queryPage',
    method: 'get',
    params: {
      current,
      size,
      insuranceName,
      insuranceCompanyId,
      sourceType,
      terminalName,
      policyNo,
      policyStatus,
      billType,
      issueName,
      issuePhone,
      holderName,
      sellerName,
      startProposalTime,
      endProposalTime
    }
  });
}
// 保险订单管理-保险销售单-分页列表-按交易记录查
export function insurancePayList(
  current,
  size,
  insuranceName, // 保险名称
  insuranceCompanyId, // 保险服务商ID
  sourceType, // 来源类型 0 线上 1 线下
  terminalName, // 保单来源终端
  policyNo, // 保司保单号
  policyStatus, // 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
  billType, // 定额方案类型
  issueName, // 被保人姓名
  issuePhone, // 被保人电话
  holderName, // 投保人姓名
  sellerName, // 销售员姓名
  startPayTime, // 交费日期起
  endPayTime, // 交费日期止
  transactionId // 交易流水号
) {
  return request({
    url: '/hmc/api/v1/insurance/pay/queryPage',
    method: 'get',
    params: {
      current,
      size,
      insuranceName,
      insuranceCompanyId,
      sourceType,
      terminalName,
      policyNo,
      policyStatus,
      billType,
      issueName,
      issuePhone,
      holderName,
      sellerName,
      startPayTime,
      endPayTime,
      transactionId
    }
  });
}
// 保险订单管理-保险销售单-保险提供商下拉列表
export function insuranceRecordCompanyList() {
  return request({
    url: '/hmc/api/v1/insurance/record/companyList',
    method: 'get',
    params: {
    }
  });
}
// 保险订单管理-保险销售单-按保单查-详情-保单信息
export function insuranceRecordDetail(
  id // 保单id
) {
  return request({
    url: '/hmc/api/v1/insurance/record/detail',
    method: 'post',
    data: {
      id
    }
  });
}
// 保险订单管理-保险销售单-按交易记录查-详情-保单信息
export function insuranceRecordPayDetail(
  recordPayId // 支付单id
) {
  return request({
    url: '/hmc/api/v1/insurance/pay/detail',
    method: 'post',
    data: {
      recordPayId
    }
  });
}
// 保险订单管理-保险销售单-详情-保单兑付记录列表
export function insuranceRecordcCashDetail(
  current,
  size,
  insuranceRecordId // 保险销售记录id
) {
  return request({
    url: '/hmc/api/v1/insurance/record/queryCashPage',
    method: 'get',
    params: {
      current,
      size,
      insuranceRecordId // 保险销售记录id
    }
  });
}
// 保险订单管理-保险销售单-详情-退保明细
export function insuranceRetreatDetail(
  insuranceRecordId // 保险销售记录id
) {
  return request({
    url: '/hmc/api/v1/insurance/record/getRetreatDetail',
    method: 'get',
    params: {
      insuranceRecordId // 保险销售记录id
    }
  });
}
// 保险订单管理-商品订单-分页列表
export function insuranceGoodsOrderList(
  current,
  size,
  goodsName, // 商品名称
  eid, // 药品服务终端id
  orderStatus, // 订单状态:1-预订单待支付/2-已取消/3-待自提/4-待发货/5-待收货/6-已收货/7-已完成/8-取消已退
  receiveUserName, // 收货人姓名
  receiveUserTel, // 收货人手机号
  startTime, // 日期段-开始时间
  stopTime // 日期段-截止时间
) {
  return request({
    url: '/hmc/api/v1/order/pageList',
    method: 'post',
    data: {
      current,
      size,
      goodsName,
      eid,
      orderStatus,
      receiveUserName,
      receiveUserTel,
      startTime,
      stopTime
    }
  });
}
// 保险订单管理-商品订单-详情
export function insuranceGoodsOrderDetail(
  orderId
) {
  return request({
    url: '/hmc/api/v1/order/queryDetail',
    method: 'get',
    params: {
      orderId
    }
  });
}
// 保险订单管理-保险销售单-详情-查看电子保单
export function downloadPolicyFile(
  insuranceRecordId
) {
  return request({
    url: '/hmc/api/v1/insurance/record/downloadPolicyFile',
    method: 'get',
    params: {
      insuranceRecordId
    }
  })
}
// 获取商品订单列表。
export function insuranceGoodsList(
  current,
  size,
  eid,
  beginTime,
  endTime,
  goodsName,
  mobile,
  name,
  nickName,
  orderNo,
  orderStatus,
  //支付状态:1-未支付，2-已支付,3-已全部退款
  paymentStatus,
  //IH 处方编号
  ihPrescriptionNo,
  //处方类型 1：西药 0：中药
  prescriptionType
) {
  return request({
    url: '/hmc/api/v1/marketOrder/queryOrderPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      beginTime,
      endTime,
      goodsName,
      mobile,
      name,
      nickName,
      orderNo,
      orderStatus,
      paymentStatus,
      ihPrescriptionNo,
      prescriptionType
    }
  });
}
// 获取商品订单终端 
export function marketOrderEnterprise(
  current,
  size,
  ename
) {
  return request({
    url: '/hmc/api/v1/marketOrder/pageEnterprise',
    method: 'post',
    data: {
      current,
      size,
      ename
    }
  });
}
// 获取商品订单详情
export function insuranceGoodsListDetail(
  id
) {
  return request({
    url: '/hmc/api/v1/marketOrder/queryOrder',
    method: 'get',
    params: {
      id
    }
  });
}
// 发货/修改物流信息
export function editGoodsOrderDelivery(
  id,
  deliveryCompany,
  deliverNo
) {
  return request({
    url: '/hmc/api/v1/marketOrder/orderDelivery',
    method: 'post',
    data: {
      id,
      deliveryCompany,
      deliverNo
    }
  });
}
// 添加平台运营备注
export function addPlatformRemark(
  id,
  platformRemark
) {
  return request({
    url: '/hmc/api/v1/marketOrder/addPlatformRemark',
    method: 'post',
    data: {
      id,
      platformRemark
    }
  });
}