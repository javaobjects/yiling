import request from '@/subject/pop/utils/request'

// 获取集团总额度
export function getCompanyBossPeriod() {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/getCompanyDetail',
    method: 'get',
    params: {
    }
  })
}

// 保存集团总额度
export function saveCompanyBossPeriod(
  // 账期总额度
  totalAmount,
  // 已还款额度
  repaymentAmount,
  // 可使用额度
  availableAmount,
  // 已使用额度
  usedAmount
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/saveOrUpdateCompanyDetail',
    method: 'post',
    data: {
      availableAmount,
      totalAmount,
      usedAmount,
      repaymentAmount
    }
  })
}

// 获取账期列表
export function getPeriodList(
  current,
  size,
  // 状态：0-全部 1-启用 2-停用
  status,
  // 采购商
  customerName,
  // 1-账期额度管理列表 2-采购商账期列表
  type,
  // 授信名称
  ename
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/yilLingPageList',
    method: 'post',
    data: {
      current,
      customerName,
      status,
      size,
      type,
      ename
    }
  })
}

// 创建新账期企业
export function createCompanyPeriod(
  // 采购商id
  customerEid,
  // 供应商ID
  eid = '',
  // 账期额度 或 可用额度
  totalAmount,
  // 还款周期
  period,
  // 状态
  status,
  startTime,
  endTime,
  // 是否长期有效
  longEffective
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/yilLingCreate',
    method: 'post',
    data: {
      customerEid,
      eid,
      totalAmount,
      period,
      status,
      startTime,
      endTime,
      longEffective
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
  // 状态
  status,
  startTime,
  endTime,
  longEffective
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/update',
    method: 'post',
    data: {
      id,
      totalAmount,
      period,
      status,
      startTime,
      endTime,
      longEffective
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
  startTime,
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/expireDayOrderPage',
    method: 'post',
    data: {
      current,
      buyerEname,
      sellerEname,
      size,
      repaymentStatus,
      startTime,
      endTime
    }
  })
}

// 获取已使用（已还款）订单列表
export function getUsedList(
  type,
  accountId,
  status,
  current,
  size,
  startTime,
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/quotaOrderPage',
    method: 'post',
    data: {
      type,
      accountId,
      status,
      current,
      startTime,
      endTime,
      size
    }
  })
}

// 获取已使用（已还款）订单统计数据
export function getUsedListTotalData(
  type,
  accountId,
  status,
  current,
  size,
  startTime,
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/getRepaymentOrderAmount',
    method: 'post',
    data: {
      type,
      accountId,
      status,
      current,
      startTime,
      endTime,
      size
    }
  })
}

// 获取申请临时额度列表
export function getIsApplyList(
  current,
  size,
  // 采购商名称
  customerName,
  // 账期状态
  status
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/shortTimeQuotaPage',
    method: 'post',
    data: {
      current,
      status,
      customerName,
      size
    }
  })
}

// 申请临时额度审核
export function setIsApplyStatus(
  accountId,
  status
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/checkQuota',
    method: 'post',
    data: {
      accountId,
      status
    }
  })
}

// 临时额度申请
export function periodApply(
  accountId,
  // 额度
  temporaryAmount
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/applyQuota',
    method: 'post',
    data: {
      accountId,
      temporaryAmount
    }
  })
}

// 获取授信主体
export function getCompanyPeriodMainPartList() {
  return request({
    url: '/admin/pop/api/v1/enterprise/paymentDays/mainPartList',
    method: 'get',
    params: {
    }
  })
}
