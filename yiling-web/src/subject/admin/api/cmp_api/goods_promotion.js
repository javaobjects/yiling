// 商品推广活动
import request from '@/subject/admin/utils/request'
// 商品推广活动列表
export function pageList(
  // 活动名称
  activityName,
  current,
  size
) {
  return request({
    url: '/hmc/api/v1/activity/goodsPromote/PageList',
    method: 'post',
    data: {
      activityName,
      current,
      size
    }
  })
}
// 删除患教活动
export function delActivityById(
  id
) {
  return request({
    url: '/hmc/api/v1/activity/goodsPromote/delActivity',
    method: 'post',
    data: {
      id
    }
  })
}
// 保存商品推广活动
export function saveActivity(
  // 活动描述
  activityDesc,
  // 活动名称
	activityName,
  // 开始时间
  beginTime,
  // 结束时间
  endTime,
  id
) {
  return request({
    url: '/hmc/api/v1/activity/goodsPromote/saveOrUpdateGoodsPromote',
    method: 'post',
    data: {
      activityDesc,
      activityName,
      beginTime,
      endTime,
      id
    }
  })
}
// 商品推广活动详情
export function queryActivityById(
	id
) {
  return request({
    url: '/hmc/api/v1/activity/goodsPromote/queryActivityById',
    method: 'post',
    data: {
      id
    }
  })
}
// 商品推广活动医生列表
export function queryActivityDoctorPage(
  // 活动id
  activityId,
  // 第几页，默认：1
  current,
  // 第一职业医院id
  hospitalId,
  // 医生姓名
  name,
  // 每页记录数，默认：10
  size
) {
  return request({
    url: '/hmc/api/v1/activity/goodsPromote/queryActivityDocPage',
    method: 'post',
    data: {
      activityId,
      current,
      hospitalId,
      name,
      size
    }
  })
}
// 查询第一执业机构
export function queryVocationalHospitalList(
  //执业机构名称
  hospitalName
) {
  return request({
    url: '/hmc/api/v1/doctor/queryVocationalHospitalList',
    method: 'get',
    params: {
      hospitalName
    }
  })
}
// 分页查询(八子补肾活动选择医生)
export function doctorPage(
  // 当前活动id
  activityId,
  //医院id
  hospitalId,
  // 医生姓名
  name,
  // 来源：数据来源 0用户自主完善 1销售助手APP 2运营人员导入或创建
  source,
  // 开始时间
  startTime,
  // 结束时间
  endTime,
  // 第几页，默认：1
  current,
  // 每页多少数据
  size
) {
  return request({
    url: '/hmc/api/v1/doctor/activityBaZi/docPatient',
    method: 'get',
    params: {
      activityId,
      hospitalId,
      name,
      source,
      startTime,
      endTime,
      current,
      size
    }
  })
}
// 保存活动医生
export function doctorSave(
  //活动id
  activityId,
  //医生活动码集合
  hmcActivityDoctorQrcodeUrlFormList
) {
  return request({
    url: '/hmc/api/v1/activity/goodsPromote/save',
    method: 'post',
    data: {
      activityId,
      hmcActivityDoctorQrcodeUrlFormList
    }
  })
}
// 移除活动医生
export function deleteActivityDoctor(
  // 活动id
  activityId,
  // 医生id
  doctorId
) {
  return request({
    url: '/hmc/api/v1/activity/goodsPromote/delActivityDoctor',
    method: 'post',
    data: {
      activityId,
      doctorId
    }
  })
}