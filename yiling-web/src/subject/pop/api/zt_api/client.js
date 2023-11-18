import request from '@/subject/pop/utils/request'

//客户列表
export function getCustomerList(
  current,
  size,
  customerGroupId,
  name,
  customerCode,
  type,
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber//统一信用代码
) {
  return request({
    url: '/admin/dataCenter/api/v1/customer/pageList',
    method: 'post',
    data: {
      current,
      size,
      customerGroupId,
      name,
      customerCode,
      type,
      provinceCode,
      cityCode,
      regionCode,
      licenseNumber
    }
  })
}

// 获取单个客户信息
export function getCustomerDetail(customerEid, currentEid) {
  return request({
    url: '/admin/dataCenter/api/v1/customer/get',
    method: 'get',
    params: {
      customerEid,
      currentEid
    }
  })
}

// 修改使用产品线
export function updateLine(customerEid, customerName, useLineList) {
  return request({
    url: '/admin/dataCenter/api/v1/customer/updateLine',
    method: 'post',
    data: {
      customerEid,
      customerName,
      useLineList
    }
  })
}