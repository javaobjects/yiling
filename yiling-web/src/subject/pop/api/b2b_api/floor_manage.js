import request from '@/subject/pop/utils/request'
//所有商品列表
export function b2bList(
  ////商品状态0 全部 1上架 2下架 3待设置
  goodsStatus,
  //商品名称
  name,
  //批准文号
  licenseNo,
  //生产厂家
  manufacturer,
  current,
  size
) {
  return request({
    url: '/admin/b2b/api/v1/goods/list',
    method: 'post',
    data: {
      goodsStatus,
      name,
      licenseNo,
      manufacturer,
      current,
      size
    }
  })
}
//查询楼层分页列表
export function queryListPage(
  //楼层名称
  name,
  //楼层状态
  status,
  //开始创建时间
  startCreateTime,
  //结束创建时间
  endCreateTime,
  current,
  size
) {
  return request({
    url: '/admin/b2b/api/v1/shopFloor/queryListPage',
    method: 'post',
    data: {
      name,
      status,
      startCreateTime,
      endCreateTime,
      current,
      size
    }
  })
}
//保存楼层
export function floorSave(
  id,
  //楼层名称
	name,
  //排序值
  sort,
  //楼层状态：1-启用 2-停用
  status,
  //店铺商品信息
	shopGoodsList
) {
  return request({
    url: '/admin/b2b/api/v1/shopFloor/save',
    method: 'post',
    data: {
      id,
      name,
      sort,
      status,
      shopGoodsList
    }
  })
}
//删除楼层
export function floorDelete(
  id
) {
  return request({
    url: '/admin/b2b/api/v1/shopFloor/delete',
    method: 'get',
    params: {
      id
    }
  })
}
//修改起停用状态
export function updateStatus(
  id,
  //楼层状态：1-启用 2-停用
  status
) {
  return request({
    url: '/admin/b2b/api/v1/shopFloor/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}
//查看楼层详情
export function getFloor(
  id
) {
  return request({
    url: '/admin/b2b/api/v1/shopFloor/get',
    method: 'get',
    params: {
      id
    }
  })
}
//查看楼层详情
export function queryFloorGoodsList(
  //楼层id
  floorId
) {
  return request({
    url: '/admin/b2b/api/v1/shopFloor/queryFloorGoodsList',
    method: 'get',
    params: {
      floorId
    }
  })
}