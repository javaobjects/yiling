// 会议管理
import request from '@/subject/admin/utils/request'
// 会议分页列表
export function pageList(
  //标题
  title, 
  //引用业务线：0-全部 1-2C患者 2-医生 3-医代 4-店员
  useLine, 
  //开始创建时间
  startCreateTime, 
  //结束创建时间
  endCreateTime, 
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/cms/api/v1/meeting/pageList',
    method: 'post',
    data: {
      title,
      useLine,
      startCreateTime,
      endCreateTime,
      current,
      size
    }
  })
}
// 新增或修改会议
export function saveMeeting(
  //标题
  title, 
  //主讲人
  mainSpeaker, 
  //是否立即发布：0-否 1-是
  publishFlag, 
  //活动开始时间
  activityStartTime, 
  //活动结束时间
  activityEndTime, 
  // 报名结束时间
  applyEndTime, 
  // 活动形式：1-线上 2-线下
  activityModus, 
  //活动类型
  activityType, 
  //是否有学分：0-否 1-是
  creditFlag, 
  //学分值/人
  creditValue, 
  //是否公开：0-否 1-是
  publicFlag, 
  //引用业务线集合
  businessLineList, 
  // 封面图
  backgroundPic, 
  //内容详情
  content, 
  //内容权限 1-仅登录 2-需认证通过
  viewLimit,
  id
){
  return request({
    url: '/cms/api/v1/meeting/saveMeeting',
    method: 'post',
    data: {
      title,
      mainSpeaker,
      publishFlag,
      activityStartTime,
      activityEndTime,
      applyEndTime,
      activityModus,
      activityType,
      creditFlag,
      creditValue,
      publicFlag,
      businessLineList,
      backgroundPic,
      content,
      viewLimit,
      id
    }
  })
}
// 详情
export function getMeeting(
  //会议id
  id 
){
  return request({
    url: '/cms/api/v1/meeting/getMeeting',
    method: 'get',
    params: {
      id
    }
  })
}
// 发布/取消发布/删除
export function updateMeetingStatus(
  id,
  // 1-发布 2-取消发布 3-删除	
  opType 
){
  return request({
    url: '/cms/api/v1/meeting/updateMeetingStatus',
    method: 'post',
    data: {
      id,
      opType
    }
  })
}