import request from '@/subject/pop/utils/request'

// 查询申请返利企业账号列表
export function getApplyPageList(
  current,
  size,
  name,
  easCode,
  status,
  code
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryApplyPageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      easCode,
      status,
      code
    }
  })
}

// 查询企业账号
export function queryEasAccountPageList(
  current,
  size,
  customerName
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryEasAccountPageList',
    method: 'post',
    data: {
      current,
      size,
      customerName
    }
  })
}

// 返利申请计算
export function calculateRebateApply(
  eid,
  easCode,
  year,
  month,
  entryEid,
  entryCode
) {
  return request({
    url: '/admin/pop/api/v1/rebate/calculateRebateApply',
    method: 'post',
    data: {
      eid,
      easCode,
      year,
      month,
      entryEid,
      entryCode
    }
  })
}

// 查询返利申请明细
export function queryRebateApplyPageList(
  current,
  size,
  applyId
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryRebateApplyPageList',
    method: 'post',
    data: {
      current,
      size,
      applyId
    }
  })
}

// 查询返利申请订单明细
export function queryRebateApplyOrderDetailPageList(
  current,
  size,
  applyId
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryRebateApplyOrderDetailPageList',
    method: 'post',
    data: {
      current,
      size,
      applyId
    }
  })
}

// 品种
export function queryGoods() {
  return request({
    url: '/admin/pop/api/v1/rebate/queryGoods',
    method: 'get',
    params: {}
  })
}

// 查询兑付一级商
export function queryCashingEntList(
  applyEid
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryCashingEntList',
    method: 'get',
    params: {
      applyEid
    }
  })
}

// 返利申请保存
export function saveRebateApply(addParams) {
  return request({
    url: '/admin/pop/api/v1/rebate/saveRebateApply',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 返利申请---查询省份
export function queryLocation() {
  return request({
    url: '/admin/pop/api/v1/rebate/queryLocation',
    method: 'get',
    params: {}
  })
}

// 入账审核接口
export function auditApply(
  id,
  status,
  auditRemark
) {
  return request({
    url: '/admin/pop/api/v1/rebate/auditApply',
    method: 'post',
    data: {
      id,
      status,
      auditRemark
    }
  })
}

// 返利申请---修改入账明细
export function editApplyDetail(
  applyId,
  id,
  amount,
  entryDescribe
) {
  return request({
    url: '/admin/pop/api/v1/rebate/editApplyDetail',
    method: 'post',
    data: {
      applyId,
      id,
      amount,
      entryDescribe
    }
  })
}

//----------------企业返利使用------------------
// 查询使用申请
export function queryUseListPageList(
  current,
  size,
  name,
  easCode,
  applicantCode
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryUseListPageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      easCode,
      applicantCode
    }
  })
}

// 查询使用申请明细
export function queryUseDetailListPageList(
  current,
  size,
  useId
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryUseDetailListPageList',
    method: 'post',
    data: {
      current,
      size,
      useId
    }
  })
}

// 获取跳转冲红系统url
export function getRebateSystemUrl() {
  return request({
    url: '/admin/pop/api/v1/rebate/getRebateSystemUrl',
    method: 'post',
    data: {}
  })
}

//----------------企业返利对账------------------
// 财务-企业返利对账
export function queryFinancialRebateEntPageList(
  current,
  size,
  customerName,
  easCode,
  licenseNumber
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryFinancialRebateEntPageList',
    method: 'post',
    data: {
      current,
      size,
      customerName,
      easCode,
      licenseNumber
    }
  })
}

// 财务-查询以兑付申请
export function queryFinanceApplyListPageList(
  current,
  size,
  eid,
  easCode
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryFinanceApplyListPageList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      easCode
    }
  })
}

// 财务-查询使用申请
export function queryFinanceUseListPageList(
  current,
  size,
  eid,
  easCode
) {
  return request({
    url: '/admin/pop/api/v1/rebate/queryFinanceUseListPageList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      easCode
    }
  })
}
