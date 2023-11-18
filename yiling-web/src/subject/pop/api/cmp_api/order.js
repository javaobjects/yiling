import request from '@/subject/pop/utils/request';

// 退单分页列表
export function getReturnList(current, size) {
  return request({
    url: '/admin/hmc/api/v1/order/return/pageList',
    method: 'post',
    data: {
      current,
      size
    }
  });
}
// 退单分页中的退单详情
export function getReturnOrderDetail(
  // 退货单id
  returnId
) {
  return request({
    url: '/admin/hmc/api/v1/order/return/queryDetail',
    method: 'get',
    params: {
      returnId
    }
  });
}

//  全部药品订单
export function getAllOrderList(
  current,
  size,
  orderStatus,
  orderNo,
  startTime,
  stopTime,
  prescriptionStatus
) {
  return request({
    url: '/admin/hmc/api/v1/order/order/pageList',
    method: 'post',
    data: {
      current,
      size,
      orderStatus,
      orderNo,
      startTime,
      stopTime,
      prescriptionStatus
    }
  });
}
//  药品待处理订单
export function getPeddingOrderList(
  current,
  size,
  deliverType,
  orderNo,
  startTime,
  stopTime,
  orderStatusList
) {
  return request({
    url: '/admin/hmc/api/v1/order/order/pageList',
    method: 'post',
    data: {
      current,
      size,
      deliverType,
      orderNo,
      startTime,
      stopTime,
      orderStatusList
    }
  });
}

//  订单详情查询
export function getOrderDetail(orderId) {
  return request({
    url: '/admin/hmc/api/v1/order/order/queryDetail',
    method: 'get',
    params: {
      orderId
    }
  });
}

//  退单接口
export function returnOrder(orderId, logContent) {
  return request({
    url: '/admin/hmc/api/v1/order/return/apply',
    method: 'post',
    data: {
      orderId,
      logContent
    }
  });
}

//  保险销售记录
export function getInsuranceList(
  current,
  size,
  insuranceName,
  policyStatus,
  sellerUserId,
  policyNo,
  issueName,
  issuePhone,
  holderName,
  startPayTime,
  endPayTime
) {
  return request({
    url: '/admin/hmc/api/v1/insurance/pay/queryPage',
    method: 'get',
    params: {
      current,
      size,
      insuranceName,
      policyStatus,
      sellerUserId,
      policyNo,
      issueName,
      issuePhone,
      holderName,// 投保人姓名
      startPayTime,
      endPayTime
    }
  });
}
// 获取销售人员接口
export function getSellUser() {
  return request({
    url: '/admin/hmc/api/v1/insurance/pay/queryEmployeeList',
    method: 'get',
    params: {}
  });
}
//  订单设为已提
export function orderReceived(
  // 订单id
  orderId,
  // 保单id
  insuranceRecordId,
  //身份证后6位
  cardLastSix
) {
  return request({
    url: '/admin/hmc/api/v1/order/order/received',
    method: 'post',
    data: {
      orderId,
      insuranceRecordId,
      cardLastSix
    },
    // 接口返回特殊处理
    noErr: true
  });
}
//  订单发货
export function orderSend(orderId, deliverNo, deliverCompany, remark) {
  return request({
    url: '/admin/hmc/api/v1/order/order/deliver',
    method: 'post',
    data: {
      orderId,
      deliverNo,
      deliverCompany,
      remark
    }
  });
}

//  获取保险订单详情
export function getInsuranceDetail(recordPayId) {
  return request({
    url: '/admin/hmc/api/v1/insurance/pay/detail',
    method: 'get',
    params: {
      recordPayId
    }
  });
}
// 校验身份证后6位
export function checkCardLastSix(insuranceRecordId, cardLastSix) {
  return request({
    url: '/admin/hmc/api/v1/order/order/checkCardLastSix',
    method: 'post',
    data: {
      insuranceRecordId,
      cardLastSix
    }
  });
}

// 兑付 创建订单
export function cashingOrder(
  recordPayId,
  doctor,
  interrogationResult,
  // 处方图片
  prescriptionSnapshotUrlList,
  remark,
  //订单票据，逗号分隔
  orderReceipts
) {
  return request({
    url: '/admin/hmc/api/v1/insurance/pay/create_order',
    method: 'post',
    data: {
      recordPayId,
      doctor,
      interrogationResult,
      prescriptionSnapshotUrlList,
      remark,
      orderReceipts
    }
  });
}
// 订单票据
export function saveOrderReceipts(
  // 订单id
  orderId,
  // 订单票据
  orderReceiptsList
){
  return request({
    url: '/admin/hmc/api/v1/order/order/saveOrderReceipts',
    method: 'post',
    data: {
      orderId,
      orderReceiptsList
    }
  })
}
// 处方信息 保存
export function saveOrderPrescription(
  // 开方医生
  doctor,
  // 诊断结果
  interrogationResult,
  // 订单id
  orderId,
  // 处方图片
  prescriptionSnapshotUrlList
) {
  return request({
    url: '/admin/hmc/api/v1/order/order/saveOrderPrescription',
    method: 'post',
    data: {
      doctor,
      interrogationResult,
      orderId,
      prescriptionSnapshotUrlList
    }
  })
}
// 保险订单管理-保险销售记录-详情-查看电子保单
export function downloadPolicyFile(
  insuranceRecordId
) {
  return request({
    url: '/admin/hmc/api/v1/insurance/pay/downloadPolicyFile',
    method: 'get',
    params: {
      insuranceRecordId
    }
  })
}
// 获取商家后台，健康中心，商品订单列表
export function marketGoodsListPage(
  current,
  size,
  beginTime,
  endTime,
  goodsName,
  name,
  mobile,
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
    url: '/admin/hmc/api/v1/marketOrder/queryOrderPage',
    method: 'post',
    data: {
      current,
      size,
      beginTime,
      endTime,
      goodsName,
      name,
      mobile,
      nickName,
      orderNo,
      orderStatus,
      paymentStatus,
      ihPrescriptionNo,
      prescriptionType
    }
  })
}
// 获取商家后台，健康中心，商品订单列表 商品订单详情
export function marketGoodsListDetail(
  id
) {
  return request({
    url: '/admin/hmc/api/v1/marketOrder/queryOrder',
    method: 'get',
    params: {
      id
    }
  })
}
//  获取商家后台，健康中心，商品订单列表 商品订单详情 发货/修改物流信息
export function editGoodsOrderDelivery(
  id,
  deliveryCompany,
  deliverNo
) {
  return request({
    url: '/admin/hmc/api/v1/marketOrder/orderDelivery',
    method: 'post',
    data: {
      id,
      deliveryCompany,
      deliverNo
    }
  });
}
//获取商家后台，健康中心，商品订单列表 商品订单详情 添加平台运营备注
export function addPlatformRemark(
  id,
  merchantRemark
) {
  return request({
    url: '/admin/hmc/api/v1/marketOrder/addPlatformRemark',
    method: 'post',
    data: {
      id,
      merchantRemark
    }
  });
}