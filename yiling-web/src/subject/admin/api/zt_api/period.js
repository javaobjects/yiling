import request from '@/subject/admin/utils/request'

// 获取账期列表
export function getPeriodList(
  current,
  size,
  // 状态：0-全部 1-启用 2-停用
  status,
  // 授信名称
  ename,
  // 采购商名称
  customerName
) {
  return request({
    url: '/dataCenter/api/v1/enterprise/paymentDays/pageList',
    method: 'post',
    data: {
      current,
      ename,
      status,
      size,
      customerName
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
    url: '/dataCenter/api/v1/enterprise/paymentDays/quotaOrderPage',
    method: 'post',
    data: {
      accountId,
      status,
      current,
      startTime,
      endTime,
      size,
      type
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
    url: '/dataCenter/api/v1/enterprise/paymentDays/getRepaymentOrderAmount',
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
