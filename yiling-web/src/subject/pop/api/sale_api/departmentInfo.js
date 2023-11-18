import request from '@/subject/pop/utils/request';

//销售助手-权限管理-部门信息-分页列表
export function getDeptInfoList(
  current,
  size,
  status,
  name,
  managerName,
  provinceCode,
  cityCode,
  regionCode
) {
  return request({
    url: '/admin/salesAssistant/api/v1/department/pageList',
    method: 'post',
    data: {
      current,
      size,
      status,
      name,
      managerName,
      provinceCode,
      cityCode,
      regionCode
    }
  });
}

// 获取部门详情信息
export function getDeptDetail(id) {
  return request({
    url: '/admin/salesAssistant/api/v1/department/get',
    method: 'get',
    params: {
      id
    }
  });
}
// 获取部门员工信息
export function getDeptEmployee(
  current,
  size,
  departmentId
) {
  return request({
    url: '/admin/salesAssistant/api/v1/employee/pageList',
    method: 'post',
    data: {
      current,
      size,
      departmentId
    }
  });
}

//  添加商品弹窗列表
export function getChooseList(current, size, name, manufacturer) {
  return request({
    url: '/admin/salesAssistant/api/v1/data/goods/list',
    method: 'post',
    data: {
      current,
      size,
      name,
      manufacturer
    }
  });
}
