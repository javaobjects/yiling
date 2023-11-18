import request from '@/subject/pop/utils/request'

// 获取省市区列表
export function getAreaList(
  parentCode
) {
  return request({
    url: '/system/api/v1/location/listTreeByParentCode',
    method: 'get',
    params: {
      parentCode
    }
  })
}
