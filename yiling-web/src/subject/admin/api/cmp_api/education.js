// 患者教育活动
import request from '@/subject/admin/utils/request'
// 患教活动列表
export function pageList(
  // 活动名称
  activityName,
  current,
  size
) {
  return request({
    url: '/hmc/api/v1/activityPatientEducate/pageList',
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
    url: '/hmc/api/v1/activityPatientEducate/delActivityById',
    method: 'post',
    data: {
      id
    }
  })
}
// 保存患教活动
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
    url: '/hmc/api/v1/activityPatientEducate/save',
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
// 患教活动详情
export function queryActivityById(
	id
) {
  return request({
    url: '/hmc/api/v1/activityPatientEducate/queryActivityById',
    method: 'post',
    data: {
      id
    }
  })
}
// 医生列表
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
    url: '/hmc/api/v1/activity/doctor/queryActivityDoctorPage',
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
// 分页查询(活动选择医生)
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
  size,
  //医生id
  doctorId
) {
  return request({
    url: '/hmc/api/v1/doctor/page',
    method: 'get',
    params: {
      activityId,
      hospitalId,
      name,
      source,
      startTime,
      endTime,
      current,
      size,
      doctorId
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
    url: '/hmc/api/v1/activity/doctor/save',
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
    url: '/hmc/api/v1/activity/doctor/deleteActivityDoctor',
    method: 'post',
    data: {
      activityId,
      doctorId
    }
  })
}

//医带患活动
//医带患活动列表
export function docToPatientPageList(
  //活动名称
  activityName,
  //第几页，默认：1
	current,
  //每页记录数，默认：10
	size
) {
  return request({
    url: '/hmc/api/v1/activity/docToPatientPageList',
    method: 'post',
    data: {
      activityName,
      current,
      size
    }
  })
}
// 创建活动 
export function saveOrUpdateDocToPatient(
  //活动名称
  activityName,
  //开始时间
  beginTime,
  //结束时间
	endTime,
  //运营备注
	activityRemark,
  //限制医生类型：1-手动配置
	restrictDocType,
	//限制用户类型：1-平台新用户（没有注册过的），2-不限制
	restrictUserType,
  //审核用户类型：1-需要审核，2-无需审核
	auditUserType,
  //图片
  activityHeadPic,
  //活动描述
  activityDesc,
	id
  
) {
  return request({
    url: '/hmc/api/v1/activity/saveOrUpdateDocToPatient',
    method: 'post',
    data: {
      activityName,
      beginTime,
      endTime,
      activityRemark,
      restrictDocType,
      restrictUserType,
      auditUserType,
      activityHeadPic,
      activityDesc,
      id
    }
  })
}
// 医带患活动详情
export function activityQueryActivityById(
  //活动id
  id
) {
  return request({
    url: '/hmc/api/v1/activity/queryActivityById',
    method: 'post',
    data: {
      id
    }
  })
}
//查询活动下的医生
export function activityActivityDoctorPage(
  //活动id
  activityId,
  //医生id
  doctorId,
  //医生姓名
  name,
  //第一执业机构
  hospitalId,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size
) {
  return request({
    url: '/hmc/api/v1/activity/queryActivityDocPage',
    method: 'post',
    data: {
      activityId,
      doctorId,
      name,
      hospitalId,
      current,
      size
    }
  })
}

// 保存活动医生
export function activitySave(
   //活动id
   activityId,
   //医生活动码集合
   hmcActivityDoctorQrcodeUrlFormList
) {
  return request({
    url: '/hmc/api/v1/activity/save',
    method: 'post',
    data: {
      activityId,
      hmcActivityDoctorQrcodeUrlFormList
    }
  })
}
// 医带患活动患者列表
export function queryActivityDocPatientPage(
  //活动id
  activityId,
  //患者名称
  patientName,
  //医生id
  doctorId,
  //医生姓名
  doctorName,
  //凭证状态 全部-0或者null 1-待上传 2-待审核 3-审核通过 4-审核驳回
  certificateState,
  current,
  size
) {
 return request({
   url: '/hmc/api/v1/activity/queryActivityDocPatientPage',
   method: 'post',
   data: {
    activityId,
    patientName,
    doctorId,
    doctorName,
    certificateState,
    current,
    size
   }
 })
}
// 停用活动
export function stopActivity(
  //活动id
  id
) {
  return request({
    url: '/hmc/api/v1/activity/stopActivity',
    method: 'post',
    data: {
      id
    }
  })
}
//医带患活动患者详情
export function queryActivityDocPatientDetail(
  //活动id
  id
) {
  return request({
    url: '/hmc/api/v1/activity/queryActivityDocPatientDetail',
    method: 'post',
    data: {
      id
    }
  })
}
//医带患活动患者审核
export function activityDocPatientAudit(
  //患者id
  id,
  //审核结果 1-通过 2-驳回
  checkResult,
  //驳回原因
  rejectReason
) {
  return request({
    url: '/hmc/api/v1/activity/activityDocPatientAudit',
    method: 'post',
    data: {
      id,
      checkResult,
      rejectReason
    }
  })
}
//分页查询医带患 活动下的医生
export function activityDoctorPage(
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
  size,
  //医生id
  doctorId
) {
  return request({
    url: '/hmc/api/v1/doctor/activity/docPatient',
    method: 'get',
    params: {
      activityId,
      hospitalId,
      name,
      source,
      startTime,
      endTime,
      current,
      size,
      doctorId
    }
  })
}
//切换医生活动资格 
export function switchActivityDoctorQuality(
  //活动id
  activityId,
  //医生id
  doctorId,
  //医生资格 1恢复 2取消
  doctorStatus
) {
  return request({
    url: '/hmc/api/v1/activity/switchActivityDoctorQuality',
    method: 'post',
    data: {
      activityId,
      doctorId,
      doctorStatus
    }
  })
}