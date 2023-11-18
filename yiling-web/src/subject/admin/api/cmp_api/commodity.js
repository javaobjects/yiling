import request from '@/subject/admin/utils/request'
// 商品管理

// 商家列表查询
export function enterpriseList(
  current, //第几页，默认：1
  name, //企业名称
  size //每页记录数，默认：10
){
  return request({
    url: '/hmc/api/v1/hmc/goods/enterpriseList',
    method: 'get',
    params: {
      current,
      name,
      size
    }
  })
}
// 商家商品列表
export function detailsList(
  eid, //企业ID
  current, //第几页，默认：1
  name, //企业名称
  size, //每页记录数，默认：10
  goodsStatus //商品状态 1上架 2下架
){
  return request({
    url: '/hmc/api/v1/hmc/goods/list',
    method: 'get',
    params: {
      eid,
      current,
      name,
      size,
      goodsStatus
    }
  })
}