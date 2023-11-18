// 用户管理
import request from '@/subject/admin/utils/request'
// 就诊人数据
export function queryPatientPage(
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size, 
  //	姓名
  patientName, 
  // 1男 0 女
  gender, 
  //年龄开始	
  startPatientAge, 
  //年龄结束
  endPatientAge, 
  //省
  provinceCode, 
  //市
  cityCode, 
  //区
  regionCode, 
  startTime,
  endTime
){
  return request({
    url: '/hmc/api/v1/patient/queryPatientPage',
    method: 'get',
    params: {
      current,
      size,
      patientName,
      gender, 
      startPatientAge,
      endPatientAge,
      provinceCode,
      cityCode,
      regionCode,
      startTime,
      endTime
    }
  })
}
// 注册用户列表
export function queryUserPage(
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size, 
  //注册开始时间
  registBeginTime, 
  //注册结束时间
  registEndTime, 
  // 活动名称
  activityName,
  // 活动id
  activityId,
  //推荐来源
  registerSource,
  //医生id
  doctorId,
  //医生姓名
  doctorName
){
  return request({
    url: '/hmc/api/v1/user/queryUserPage',
    method: 'get',
    params: {
      current,
      size,
      registBeginTime,
      registEndTime,
      activityName,
      activityId,
      registerSource,
      doctorId,
      doctorName
    }
  })
}
// 患者绑定医生
export function queryPatientBindDoctorByPatientId(
  //就诊人id
  patientId
){
  return request({
    url: '/hmc/api/v1/patient/queryPatientBindDoctorByPatientId',
    method: 'post',
    data: {
      patientId
    }
  })
}