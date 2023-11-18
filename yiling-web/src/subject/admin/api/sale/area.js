import request from '@/subject/admin/utils/request'

// 获取省市区列表
export function getAreaList(
  parentCode
) {
  return request({
    url: '/sa/api/v1/task/listByParentCode',
    method: 'get',
    params: {
      parentCode
    }
  })
}
