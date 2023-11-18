import request from '@/subject/pop/utils/request'

// 获取采购订单数目
export function getOrderNums() {
  return request({
    url: '/admin/pop/api/v1/order/get/number',
    method: 'get',
    params: {}
  })
}

// 获取采购订单列表
export function getOrderList(
  current,
  size,
  // 订单类型：1-销售订单 2-采购订单
  type,
  // 企业名称
  name,
  // 订单号
  orderNo,
  // 订单ID
  id,
  // 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
  orderStatus,
  // 支付方式：1-线下支付 2-账期 3-预付款
  paymentMethod,
  // 支付状态：1-待支付 2-已支付
  paymentStatus,
  // 发票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
  invoiceStatus,
  startCreateTime,
  endCreateTime,
  // 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
  departmentType
) {
  return request({
    url: '/admin/pop/api/v1/order/get/page',
    method: 'post',
    data: {
      current,
      name,
      type,
      size,
      orderNo,
      id,
      orderStatus,
      paymentMethod,
      paymentStatus,
      invoiceStatus,
      startCreateTime,
      endCreateTime,
      departmentType
    }
  })
}

// 获取采购订单详情
export function getOrderDetail(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/purchase/get/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 上传回执单信息
export function uploadReceipt(orderId, receiveReceiptList) {
  return request({
    url: '/admin/pop/api/v1/order/upload/receipt',
    method: 'post',
    data: {
      // 订单号
      orderId,
      // 上传图片返回的key数组
      receiveReceiptList
    }
  })
}

// 采购订单确认收货
export function orderReceive(
  // 订单id
  orderId,
  // 商品信息
  orderReceiveGoodsInfoList,
  // 回执单
  receiveReceiptList
) {
  return request({
    url: '/admin/pop/api/v1/order/receive',
    method: 'post',
    data: {
      orderId,
      orderReceiveGoodsInfoList,
      receiveReceiptList
    }
  })
}

// 查看发票信息
export function checkOrderTicket(
  // 订单id
  orderId
) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/get/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 采购订单退货
export function orderReject(
  // 订单id
  orderId,
  // 商品信息
  orderDetailList,
  // 退货类型
  returnType,
  // 退货备注
  remark
) {
  return request({
    url: '/admin/pop/api/v1/refund/purchase/apply/returnOrder',
    method: 'post',
    data: {
      orderId,
      orderDetailList,
      returnType,
      remark
    }
  })
}

// 获取退货订单详情
export function getRejectOrderDetail(returnOrderId) {
  return request({
    url: '/admin/pop/api/v1/refund/buyerOrderReturnDetail',
    method: 'get',
    params: {
      returnOrderId
    }
  })
}

// 获取退货订单列表
export function getRejectOrderList(
  current,
  size,
  // 订单号
  orderNo,
  // 订单ID
  orderId,
  // 退货单号
  orderReturnNo,
  // 订单状态 1-待审核 2-审核通过 3-审核驳回
  returnStatus,
  // 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
  returnType,
  // 供应商名称
  sellerEname,
  // 开始时间
  startTime,
  // 结束时间
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/refund/buyerOrderReturnPageList',
    method: 'post',
    data: {
      current,
      size,
      orderNo,
      orderId,
      orderReturnNo,
      returnStatus,
      returnType,
      sellerEname,
      startTime,
      endTime
    }
  })
}

// 获取采购退货单订单数目
export function getRejectOrderNums() {
  return request({
    url: '/admin/pop/api/v1/refund/get/number/buyer',
    method: 'get',
    params: {}
  })
}

// 销售退货单数量接口
export function getSellerOrderNums(
  // 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
  departmentType
) {
  return request({
    url: '/admin/pop/api/v1/refund/get/number/seller',
    method: 'get',
    params: {
      departmentType
    }
  })
}

// 获取预订单列表
export function getPreOrderList(
  current,
  size,
  // 供应商名称
  distributorEname,
  // 单号
  orderNo,
  // 订单Id
  id,
  // 审核状态： 1-未提交 2-待审核 3-审核通过 4-审核驳回
  auditStatus,
  // 开始时间
  startCreatTime,
  // 结束时间
  endCreatTime
) {
  return request({
    url: '/admin/pop/api/v1/order/expect/get/page',
    method: 'post',
    data: {
      current,
      size,
      distributorEname,
      orderNo,
      id,
      auditStatus,
      startCreatTime,
      endCreatTime
    }
  })
}

// 预订单取消订单
export function preOrderCancle(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/expect/cancel',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 预订单内部取消(待审核状态订单)
export function preOrderSuperCancle(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/super/cancel',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 获取预订单详情
export function getPreOrderDetail(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/expect/get/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// ----------销售订单---------------
// 获取销售订单数目
export function getSaleOrderNums(
  // 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
  departmentType
) {
  return request({
    url: '/admin/pop/api/v1/order/get/sale/number',
    method: 'get',
    params: {
      departmentType
    }
  })
}

// 获取订单审核列表
export function getOrderReviewList(
  departmentType,
  current,
  size,
  // 采购商名称
  buyerEname,
  // 订单号
  orderNo,
  // 订单id
  id,
  // 支付方式：1-线下支付 2-账期 3-预付款
  paymentMethod,
  // 审核状态
  auditStatus,
  // 开始时间
  startCreateTime,
  // 结束时间
  endCreateTime,
  //省
  provinceCode,
  //市
  cityCode,
  //区
  regionCode,
  // 商务联系人名称
  contacterName,
  // 部门id
  departmentIdCode
) {
  return request({
    url: '/admin/pop/api/v1/order/manage/get/page',
    method: 'post',
    data: {
      departmentType,
      current,
      size,
      buyerEname,
      orderNo,
      id,
      paymentMethod,
      auditStatus,
      startCreateTime,
      endCreateTime,
      provinceCode,
      cityCode,
      regionCode,
      contacterName,
      departmentIdCode
    }
  })
}

// 获取审核订单详情
export function getOrderReviewDetail(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/manage/get/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 销售订单列表取消订单
export function orderCancle(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/cancel',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 销售订单详情
export function getSellOrderDetail(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/sale/get/detail',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 审核订单驳回
export function orderReviewReject(id, auditRejectReason) {
  return request({
    url: '/admin/pop/api/v1/order/manage/reject',
    method: 'get',
    params: {
      id,
      auditRejectReason
    }
  })
}

// 销售订单发货
export function orderDelivery(
  // 订单id
  orderId,
  // 物流类型：1-自有物流 2-第三方物流
  deliveryType,
  // 商品信息
  orderDeliveryGoodsInfoList,
  // 物流公司
  deliveryCompany,
  // 物流单号
  deliveryNo
) {
  return request({
    url: '/admin/pop/api/v1/order/delivery',
    method: 'post',
    data: {
      orderId,
      deliveryType,
      orderDeliveryGoodsInfoList,
      deliveryCompany,
      deliveryNo
    }
  })
}

// 审核订单通过
export function orderReviewResole(id) {
  return request({
    url: '/admin/pop/api/v1/order/manage/review/pass',
    method: 'get',
    params: {
      id
    }
  })
}

// 获取审核订单数量
export function getOrderReviewNum(departmentType) {
  return request({
    url: '/admin/pop/api/v1/order/manage/get/number',
    method: 'get',
    params: {
      departmentType
    }
  })
}

// ---------------发票管理---------
// 发票管理列表
export function getInvoiceList(
  current,
  size,
  // 采购商名称
  buyerEname,
  // 销售主体
  distributorEid,
  invoiceNo,
  orderNo,
  // 订单ID
  id,
  invoiceStatus,
  orderStatus,
  paymentMethod,
  startInvoiceTime,
  endInvoiceTime,
  // 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
  departmentType,
  // 下单时间
  startOrderTime,
  endOrderTime,
  // 省市区
  provinceCode,
  cityCode,
  regionCode
) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/manage',
    method: 'post',
    data: {
      current,
      size,
      buyerEname,
      distributorEid,
      invoiceNo,
      orderNo,
      id,
      invoiceStatus,
      orderStatus,
      paymentMethod,
      startInvoiceTime,
      endInvoiceTime,
      departmentType,
      startOrderTime,
      endOrderTime,
      provinceCode,
      cityCode,
      regionCode
    }
  })
}

// 获取以岭主体企业列表
export function getAgreementMainPartList() {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/mainPart/list',
    method: 'get',
    params: {}
  })
}

// 申请发票页面信息
export function getApplyInvoiceDetail(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/get/apply',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 申请发票页面信息-订单支付总额/发票总额 字段数据
export function getApplyInvoiceTotalAmount(list) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/get/amount',
    method: 'post',
    data: {
      list
    }
  })
}

// 申请发票页面信息
export function getApplyInvoiceDetailChange(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/change/ticket/type',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 获取最开票限制金额
export function getInvoiceMaxAmount(orderId, transitionRuleCode) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/get/max/amount',
    method: 'get',
    params: {
      orderId,
      transitionRuleCode
    }
  })
}

// 申请发票提交
export function submitInvoice(
  id,
  ticketDiscountFlag,
  ticketDiscountNoList,
  ticketDiscount,
  transitionRuleCode,
  invoiceSummary,
  erpDeliveryNoList,
  invoiceEmail
) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/apply/submit',
    method: 'post',
    data: {
      id,
      ticketDiscountFlag,
      ticketDiscountNoList,
      ticketDiscount,
      transitionRuleCode,
      invoiceSummary,
      erpDeliveryNoList,
      invoiceEmail
    }
  })
}

// 修改发票提交
export function updateInvoice(
  id,
  ticketDiscountFlag,
  ticketDiscountNoList,
  ticketDiscount,
  transitionRuleCode,
  invoiceForm,
  groupDeliveryNos,
  invoiceEmail
) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/change/submit',
    method: 'post',
    data: {
      id,
      ticketDiscountFlag,
      ticketDiscountNoList,
      ticketDiscount,
      transitionRuleCode,
      invoiceForm,
      groupDeliveryNos,
      invoiceEmail
    }
  })
}

// 选择票折计算金额
export function computeInvoiceAmount(
  orderId,
  detailId,
  invoiceDiscountType,
  value,
  erpDeliveryNo,
  goodsPrice,
  batchFormList
) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/compute/amount',
    method: 'post',
    data: {
      orderId,
      detailId,
      invoiceDiscountType,
      value,
      erpDeliveryNo,
      goodsPrice,
      batchFormList
    }
  })
}

// 选择票折
export function getInvoiceChoiceList(
  orderId
) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/choice/invoice',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 使用票折订单个数信息
export function getInvoiceUseOrder(ticketDiscountNo) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/use/invoice',
    method: 'get',
    params: {
      ticketDiscountNo
    }
  })
}
// ----------销售订单 发票列表页面--------------
export function getInvoiceApplyList(orderId) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/apply/list',
    method: 'get',
    params: {
      orderId
    }
  })
}

// 发票详情页面
export function getInvoiceApplyDetail(applyId) {
  return request({
    url: '/admin/pop/api/v1/order/invoice/apply/detail',
    method: 'get',
    params: {
      applyId
    }
  })
}

// ----------销售订单 退货单--------------
// 销售退货单列表
export function getReturnGoodsList(
  current,
  size,
  // 订单号
  orderNo,
  // 订单ID
  orderId,
  // 退货单号
  orderReturnNo,
  // 订单状态 1-待审核 2-审核通过 3-审核驳回
  returnStatus,
  // 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
  returnType,
  // 采购商名称
  buyerEname,
  // 开始时间
  startTime,
  // 结束时间
  endTime,
  // 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
  departmentType
) {
  return request({
    url: '/admin/pop/api/v1/refund/sellerOrderReturnPageList',
    method: 'post',
    data: {
      current,
      size,
      orderNo,
      orderId,
      orderReturnNo,
      returnStatus,
      returnType,
      buyerEname,
      startTime,
      endTime,
      departmentType
    }
  })
}

// 销售退货单详情
export function getSellerOrderReturnDetail(
  returnOrderId
) {
  return request({
    url: '/admin/pop/api/v1/refund/sellerOrderReturnDetail',
    method: 'get',
    params: {
      returnOrderId
    }
  })
}

// 退货单申请驳回
export function returnOrderReject(
  orderId,
  orderReturnId,
  remark,
  orderDetailList
) {
  return request({
    url: '/admin/pop/api/v1/refund/returnOrder/reject',
    method: 'post',
    data: {
      orderId,
      orderReturnId,
      remark,
      orderDetailList
    }
  })
}

// 退货单审核
export function checkOrderReturn(
  orderId,
  // 退货单id
  orderReturnId,
  remark,
  // 商品信息
  orderDetailList
) {
  return request({
    url: '/admin/pop/api/v1/refund/checkOrderReturn',
    method: 'post',
    data: {
      orderId,
      orderReturnId,
      remark,
      orderDetailList
    }
  })
}

// 流向管理
// 一级  以岭流向列表
export function flowDirectionPage(
  buyerChannelId, //	采购商渠道
  buyerEname, // 采购商名称
  current, // 第几页
  endCreateTime, //下单结束时间
  endDeliveryTime, //发货结束时间
  goodsName, // 商品名称
  licenseNo, // 商品批准文号
  sellSpecifications, // 商品规格
  // sellerName, // 供应商名称
  size, // 每页记录数
  startCreateTime, // 下单开始时间
  startDeliveryTime // 发货开始时间
) {
  return request({
    url: '/admin/pop/api/v1/flow/yiling/page',
    method: 'post',
    data: {
      buyerChannelId,
      buyerEname,
      current,
      endCreateTime,
      endDeliveryTime,
      goodsName,
      licenseNo,
      sellSpecifications,
      // sellerName,
      size,
      startCreateTime,
      startDeliveryTime
    }
  })
}

// 二级 供应商流向列表
export function sellerNamePage(
  buyerChannelId, //	采购商渠道
  buyerEname, // 采购商名称
  current, // 第几页
  endCreateTime, // 下单结束时间
  endDeliveryTime, // 发货结束时间
  goodsName, // 商品名称
  licenseNo, // 商品批准文号
  sellSpecifications, // 商品规格
  sellerEname, // 供应商名称
  size, // 每页记录数
  startCreateTime, // 下单开始时间
  startDeliveryTime // 发货开始时间
) {
  return request({
    url: '/admin/pop/api/v1/flow/seller/page',
    method: 'post',
    data: {
      buyerChannelId,
      buyerEname,
      current,
      endCreateTime,
      endDeliveryTime,
      goodsName,
      licenseNo,
      sellSpecifications,
      sellerEname,
      size,
      startCreateTime,
      startDeliveryTime
    }
  })
}

