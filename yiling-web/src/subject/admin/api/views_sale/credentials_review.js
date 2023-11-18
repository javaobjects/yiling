import request from '@/subject/admin/utils/request'
// 证件审核列表，
export function staffExternalAuditList(current, size, name, idNumber, auditStatus, auditUserName, auditTimeBegin, auditTimeEnd) {
  return request({
    url: '/salesAssistant/api/v1/staffExternaAudit/pageList',
    method: 'post',
    data: {
      current,
      size,
      name, // 姓名
      idNumber, // 证件
      auditStatus, // 0-全部 1-待审核 2-审核通过 3-审核驳回
      auditUserName, // 审核人姓名
      auditTimeBegin, // 审核开始时间
      auditTimeEnd // 审核结束时间
    }
  })
}

// 证件审核通过/驳回
export function reviewCredential(id, auditStatus, auditRejectReason) {
  return request({
    url: '/salesAssistant/api/v1/staffExternaAudit/audit',
    method: 'post',
    data: {
      id,
      auditStatus,
      auditRejectReason // 驳回原因
    }
  })
}

// 审核记录详情，
export function reviewDetail(id) {
  return request({
    url: '/salesAssistant/api/v1/staffExternaAudit/get',
    method: 'get',
    params: {
      id
    }
  })
}
