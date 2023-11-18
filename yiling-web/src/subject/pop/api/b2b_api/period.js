import request from '@/subject/pop/utils/request'

// 获取账期列表
export function getPeriodList(
  current,
  size,
  // 状态：0-全部 1-启用 2-停用
  status,
  // 采购商
  customerName
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/queryPaymentDaysPage',
    method: 'post',
    data: {
      current,
      customerName,
      status,
      size
    }
  })
}

// 创建新账期企业
export function createCompanyPeriod(
  // 采购商id
  customerEid,
  // 账期额度
  totalAmount,
  // 信用周期
  period,
  // 账期上浮点位（百分比）
  upPoint,
  // 状态
  status
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/addPaymentDays',
    method: 'post',
    data: {
      customerEid,
      upPoint,
      totalAmount,
      period,
      status
    }
  })
}

// 修改账期企业
export function editCompanyPeriod(
  id,
  // 账期额度 或 可用额度
  totalAmount,
  // 还款周期
  period,
  // 上浮点位
  upPoint,
  // 状态
  status
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/update',
    method: 'post',
    data: {
      id,
      totalAmount,
      period,
      status,
      upPoint
    }
  })
}

// 获取账期提醒列表
export function getPeriodWarnList(
  current,
  size,
  // 采购商名称
  buyerEname,
  // 供应商名称
  sellerEname,
  // 还款状态
  repaymentStatus,
  deliveryStartTime,
  deliveryEndTime
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/expireOrderPage',
    method: 'post',
    data: {
      current,
      buyerEname,
      sellerEname,
      size,
      repaymentStatus,
      deliveryStartTime,
      deliveryEndTime
    }
  })
}

// 获取到期提醒统计数据
export function getWarnTotalData(
  // 采购商名称
  buyerEname,
  // 供应商名称
  sellerEname,
  // 还款状态
  repaymentStatus,
  startTime,
  endTime
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/expireOrderAmountCount',
    method: 'post',
    data: {
      // 采购商名称
      buyerEname,
      // 供应商名称
      sellerEname,
      // 还款状态
      repaymentStatus,
      startTime,
      endTime
    }
  })
}

// 获取已使用订单列表
export function getUsedList(
  accountId,
  status,
  current,
  size,
  startTime,
  endTime
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/paymentDaysUsedOrderPage',
    method: 'post',
    data: {
      accountId,
      status,
      current,
      startTime,
      endTime,
      size
    }
  })
}

// 获取已还款订单列表
export function getRepaymentList(
  accountId,
  status,
  current,
  size,
  startTime,
  endTime
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/paymentDaysRepaymentOrderPage',
    method: 'post',
    data: {
      accountId,
      status,
      current,
      startTime,
      endTime,
      size
    }
  })
}

// 获取待还款订单列表
export function getNeedRepaymentList(
  accountId,
  status,
  current,
  size,
  startTime,
  endTime
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/paymentDaysUnRepaymentOrderPage',
    method: 'post',
    data: {
      accountId,
      status,
      current,
      startTime,
      endTime,
      size
    }
  })
}

// 获取订单统计数据
export function getListTotalData(
  accountId,
  status,
  type,
  startTime,
  endTime
) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/getOrderAmountCount',
    method: 'post',
    data: {
      accountId,
      status,
      startTime,
      endTime,
      type
    }
  })
}

// 查询采购商列表
export function getPeriodBuyerList(current, size, name) {
  return request({
    url: '/admin/b2b/api/v1/paymentDays/getCustomerList',
    method: 'post',
    data: {
      current,
      size,
      name
    }
  })
}
