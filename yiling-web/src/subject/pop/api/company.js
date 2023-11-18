import request from '@/subject/pop/utils/request'

// 获取当前企业信息
export function getCompanyInfo() {
  return request({
    url: '/admin/pop/api/v1/enterprise/getCurrentEnterpriseInfo',
    method: 'get',
    params: {}
  })
}

// 获取企业列表
export function getCompanyList(
  current,
  size,
  name = '',
  authStatus = 0,
  cityCode = '',
  contactor = '',
  contactorPhone = '',
  id = 0,
  licenseNumber = '',
  provinceCode = '',
  regionCode = '',
  status = 0,
  type = 0
) {
  return request({
    url: '/admin/pop/api/v1/enterprise/pageList',
    method: 'post',
    data: {
      current,
      name,
      size,
      authStatus,
      cityCode,
      contactor,
      contactorPhone,
      id,
      licenseNumber,
      provinceCode,
      regionCode,
      status,
      type
    }
  })
}

// 获取企业详情
export function getCompanyDetail(id) {
  return request({
    url: '/admin/pop/api/v1/enterprise/get',
    method: 'get',
    params: {
      id
    }
  })
}

// 修改企业信息
export function changeCompany(id, name, licenseNumber, provinceCode, cityCode, regionCode, contactor, contactorPhone) {
  return request({
    url: '/admin/pop/api/v1/enterprise/update',
    method: 'post',
    data: {
      name,
      cityCode,
      contactor,
      contactorPhone,
      id,
      licenseNumber,
      provinceCode,
      regionCode
    }
  })
}

// 更新状态
export function updateStatus(id, status) {
  return request({
    url: '/admin/pop/api/v1/enterprise/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 获取企业下账号列表
export function getCompanyUserList(
  current,
  size,
  // 企业名称
  ename = '',
  // 企业类型
  etype,
  // 手机号
  mobile = '',
  // 姓名
  name = '',
  // 状态
  status
) {
  return request({
    url: '/admin/pop/api/v1/staff/pageList',
    method: 'post',
    data: {
      current,
      size,
      ename,
      etype,
      mobile,
      name,
      status
    }
  })
}

// 修改企业下账号状态
export function updateCompanyUser(
  id,
  // 状态
  status
) {
  return request({
    url: '/admin/pop/api/v1/staff/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 修改企业下账号姓名或手机号
export function updateCompanyUserName(id, name = '', mobile = '') {
  return request({
    url: '/admin/pop/api/v1/staff/updateUserInfo',
    method: 'post',
    data: {
      id,
      name,
      mobile
    }
  })
}

// 解除企业和账号之间关系
export function unbindCompanyUser(userId, eid) {
  return request({
    url: '/admin/pop/api/v1/staff/unbindEnterprise',
    method: 'post',
    data: {
      userId,
      eid
    }
  })
}

// 获取企业下账号详情
export function getCompanyUserDetail(id = '') {
  return request({
    url: '/admin/pop/api/v1/staff/get',
    method: 'get',
    params: {
      id
    }
  })
}
