import request from '@/subject/admin/utils/request';

// 审核记录详情
export function queryPage(
  current,
  size,
  // 审核状态
  auditStatus,
  // 单据编号
  docCode,
  startTime,
  endTime,
  // 发货单位
  distributorEname
) {
  return request({
    url: '/salesAssistant/api/v1/accompanyingBill/queryPage',
    method: 'get',
    params: {
      current,
      size,
      auditStatus,
      docCode,
      startTime,
      endTime,
      distributorEname
    }
  })
}

// 审核
export function audit(
  auditStatus,
  id,
  rejectionReason
) {
  return request({
    url: '/salesAssistant/api/v1/accompanyingBill/audit',
    method: 'post',
    data: {
      auditStatus,
      id,
      rejectionReason
    }
  })
}

// 审核记录详情
export function getDetails(id) {
  return request({
    url: '/salesAssistant/api/v1/accompanyingBill/getById',
    method: 'get',
    params: {
      id
    }
  })
}

// 流向列表
export function queryMatchBillPage(
  current,
  size,
  recvEname,
  docCode,
  erpMatchStartTime,
  erpMatchEndTime,
  crmMatchStartTime,
  crmMatchEndTime,
  result
) {
  return request({
    url: '/salesAssistant/api/v1/accompanyingBill/queryMatchBillPage',
    method: 'get',
    params: {
      current,
      size,
      recvEname,
      docCode,
      erpMatchStartTime,
      erpMatchEndTime,
      crmMatchStartTime,
      crmMatchEndTime,
      result
    }
  })
}

// 审核记录详情
export function getMatchBillDetail(id) {
  return request({
    url: '/salesAssistant/api/v1/accompanyingBill/getMatchBillDetail',
    method: 'get',
    params: {
      id
    }
  })
}
