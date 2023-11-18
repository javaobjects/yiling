import request from '@/subject/pop/utils/request'

// 健康管理中心-员工管理-药代管理-药代信息-分页列表
export function getDrugAgentList(
  current,
  size,
  // 姓名
  name,
  // 手机号
  mobile,
  // 工号
  code
) {
  return request({
    url: '/admin/hmc/api/v1/mr/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      mobile,
      code
    }
  })
}
// 健康管理中心-员工管理-药代管理-药代信息-可售药品分页列表
export function salesGoodsPageList(
  current,
  size,
  // 药品名称（模糊查询）
  name,
  // 员工ID
  employeeId
) {
  return request({
    url: '/admin/hmc/api/v1/mr/salesGoodsPageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      employeeId
    }
  })
}
// 健康管理中心-员工管理-药代管理-药代信息-可售药品分页列表-添加删除
export function addOrRemoveSalesGoods(
  // 员工ID
  employeeId,
  // 商品ID列表
  goodsIds,
  // 操作类型：1-添加 2-删除
  opType
) {
  return request({
    url: '/admin/hmc/api/v1/mr/addOrRemoveSalesGoods',
    method: 'post',
    data: {
      employeeId,
      goodsIds,
      opType
    }
  })
}
// 健康管理中心-员工管理-药代管理-药代信息-详情页
export function getDrugAgentDetail(
  // 员工ID
  employeeId
) {
  return request({
    url: '/admin/hmc/api/v1/mr/detail',
    method: 'get',
    params: {
      employeeId
    }
  })
}
