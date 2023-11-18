// 药品福利
import request from '@/subject/admin/utils/request'
// 药品福利计划列表
export function queryPage(
  //第几页，默认：1
  current,
  //每页记录数，默认：10
	size
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/queryPage',
    method: 'post',
    data: {
      current,
      size
    }
  })
}
// 获取福利计划详情
export function getDrugWelfareDetailById(
  id
){
  return request({
    url: '/hmc/api/v1/drug/welfare/getDrugWelfareDetailById',
    method: 'get',
    params: {
      id
    }
  })
}
// 编辑福利计划详情
export function updateDrugWelfare(
  //开始时间
  beginTime,
  //结束时间
  endTime,
  //主键id 
  id,
  //活动名称 
  name,
  //备注 
  remark,
  //活动状态：1-启用 2-停用 
  status,
  //药品规格id
  sellSpecificationsId,
  //福利券包
	drugWelfareCouponList
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/updateDrugWelfare',
    method: 'post',
    data: {
      beginTime,
      endTime,
      id,
      name,
      remark,
      status,
      sellSpecificationsId,
      drugWelfareCouponList
    }
  })
}
// 列表查询商家下拉选单
export function queryEnterpriseList() {
  return request({
    url: '/hmc/api/v1/drug/welfare/common/queryEnterpriseList',
    method: 'post',
    data: {}
  })
}
//查询福利计划下拉选单 
export function queryDrugWelfareList() {
  return request({
    url: '/hmc/api/v1/drug/welfare/common/queryDrugWelfareList',
    method: 'post',
    data: {}
  })
}
// 药品福利计划终端商家列表
export function enterpriseQueryPage(
  // 商家id
	eid,
  // 福利计划id
	drugWelfareId,
  // 开始时间	
	startTime,
  // 结束时间
	endTime,
  // 第几页，默认：1
  current,
  // 每页记录数，默认：10
	size
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/enterprise/queryPage',
    method: 'post',
    data: {
      eid,
      drugWelfareId,
      startTime,
      endTime,
      current,
      size
    }
  })
}
// 添加商家时选择商家列表 
export function pageEnterprise(
  //商家名称
  ename,
  current,
	size
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/enterprise/pageEnterprise',
    method: 'post',
    data: {
      ename,
      current,
      size
    }
  })
} 
// 保存药品福利计划与商家关系
export function save(
  //商家名称
  ename,
  // 商家id
  eid,
  // 福利计划id	
  drugWelfareId,
  // 备注
  remake
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/enterprise/save',
    method: 'post',
    data: {
      ename,
      eid,
      drugWelfareId,
      remake
    }
  })
} 
// 删除药品福利计划与商家关系
export function enterpriseDelete(
  //福利计划与商家关系id
  id
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/enterprise/delete',
    method: 'post',
    data: {
      id
    }
  })
} 