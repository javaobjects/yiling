import request from '@/subject/admin/utils/request'
// 锁定用户列表
export function lockCustomerList(current, size, name, licenseNumber, status) {
  return request({
    url: '/salesAssistant/api/v1/lockCustomer/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      name, // 企业名称
      licenseNumber, // 执业许可证号/社会信用统一代码
      status // 1-启用 2-停用
    }
  })
}
// 添加锁定用户
export function addLockCustomer(name, type, licenseNumber, status, provinceCode, cityCode, regionCode, address, plate ) {
  return request({
    url: '/salesAssistant/api/v1/lockCustomer/addLockCustomer',
    method: 'post',
    data: {
      name, // 企业名称
      type, // 企业类型
      licenseNumber, // 执业许可证号/社会信用统一代码
      status, // 1-启用 2-停用
      provinceCode,
      cityCode,
      regionCode,
      address,
      plate
    }
  })
}
// 批量启用禁用操作
export function updateStatus(idList, status) {
  return request({
    url: '/salesAssistant/api/v1/lockCustomer/updateStatus',
    method: 'post',
    data: {
      idList, // ID集合
      status // 状态：1-启用 2-停用
    }
  })
}
