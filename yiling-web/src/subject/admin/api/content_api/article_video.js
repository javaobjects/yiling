// 内容管理
import request from '@/subject/admin/utils/request'

// 关联药品 数据
export function queryGoods(
  //药品名称
  goodsName, 
  current,
  size
){
  return request({
    url: '/cms/api/v1/cms/goods/queryGoods',
    method: 'get',
    params: {
      goodsName,
      current,
      size
    }
  })
}
export function queryDiseaseList(
  //疾病名称
  diseaseName, 
  current,
  size
){
  return request({
    url: '/cms/api/v1/cms/content/queryDiseaseList',
    method: 'get',
    params: {
      diseaseName,
      current,
      size
    }
  })
}
// 关联科室
export function queryDepartmentList(){
  return request({
    url: '/cms/api/v1/cms/content/queryDepartmentList',
    method: 'get',
    params: {}
  })
}
// 所属前台分类
export function queryCategoryList(){
  return request({
    url: '/cms/api/v1/cms/category/queryCategoryList',
    method: 'get',
    params: {
    }
  })
}

// 文章/视频 列表
export function queryContentPage(
  //类型:1-文章 2-视频
  contentType, 
  //第几页，默认：1
  current, 
  //开始时间
  startTime,
  //结束时间
  endTime, 
  //引用业务线id
  lineId, 
  //每页记录数，默认：10
  size, 
  //标题
  title,
  //内容所属医生
  docId,
  //创建人 
  createUserName,
  //状态：1-未发布 2-已发布
  status,
  //创建来源 1-运营后台，2-IH后台
  createSource,
  //内容来源: 0-所有 1-站内创建 2-外链
  sourceContentType	
){
  return request({
    url: '/cms/api/v1/cms/content/queryContentPage',
    method: 'get',
    params: {
      contentType,
      current,
      startTime,
      endTime,
      lineId,
      size,
      title,
      docId,
      createUserName,
      status,
      createSource,
      sourceContentType
    }
  })
}
// 添加
export function addContent(
  //作者
  author, 
  //栏目id
  categoryId,
  //内容
  content, 
  //类型:1-文章 2-视频
  contentType, 
  //封面
  cover, 
  //科室
  deptIdList, 
  //疾病
  diseaseIdList, 
  // 业务线
  // displayLines, 
  //草稿：1-是 0-否
  isDraft, 
  //是否公开：0-否 1-是
  isOpen, 
  //置顶:1-是 0--否
  isTop, 
  //会议id
  meetingId, 
  //来源
  source, 
  //主讲人
  speaker, 
  //关联标准库商品
  standardGoodsIdList, 
  //状态 1未发布 2立即发布
  status, 
  //副标题
  subtitle, 
  //标题
  title, 
  //视频oss key
  vedioFileUrl,
  // 内容权限 1-仅登录 2-需认证通过
  viewLimit,
  // 所属医生id
  docId,
  //内容来源:1-站内创建 2-外链
  sourceContentType,
  //H5地址
  linkUrl
){
  return request({
    url: '/cms/api/v1/cms/content/addContent',
    method: 'post',
    data: {
      author,
      categoryId,
      content,
      contentType,
      cover,
      deptIdList,
      diseaseIdList,
      // displayLines,
      isDraft,
      isOpen,
      isTop,
      meetingId,
      source,
      speaker,
      standardGoodsIdList,
      status,
      subtitle,
      title,
      vedioFileUrl,
      viewLimit,
      docId,
      sourceContentType,
      linkUrl
    }
  })
}
// 编辑
export function updateContent(
  //作者
  author, 
  //栏目id
  categoryId, 
  //内容
  content, 
  //类型:1-文章 2-视频
  contentType, 
  //封面
  cover, 
  //科室
  deptIdList, 
  //疾病
  diseaseIdList, 
  // 业务线
  // displayLines, 
  //草稿：1-是 0-否
  isDraft, 
  //是否公开：0-否 1-是
  isOpen, 
  //置顶:1-是 0--否
  isTop, 
  //会议id
  meetingId, 
  //来源
  source, 
  //主讲人
  speaker, 
  //关联标准库商品
  standardGoodsIdList, 
  //状态 1未发布 2立即发布
  status, 
  //副标题
  subtitle, 
  //标题
  title, 
  //视频oss key
  vedioFileUrl, 
  //id
  id,
  // 内容权限 1-仅登录 2-需认证通过
  viewLimit,
  // 所属医生id
  docId,
  //内容来源:1-站内创建 2-外链
  sourceContentType,
  //H5地址
  linkUrl
){
  return request({
    url: '/cms/api/v1/cms/content/updateContent',
    method: 'post',
    data: {
      author,
      categoryId,
      content,
      contentType,
      cover,
      deptIdList,
      diseaseIdList,
      // displayLines,
      isDraft,
      isOpen,
      isTop,
      meetingId,
      source,
      speaker,
      standardGoodsIdList,
      status,
      subtitle,
      title,
      vedioFileUrl,
      id,
      viewLimit,
      docId,
      sourceContentType,
      linkUrl
    }
  })
}
//编辑  置顶，发布 
export function updateContent2(
  id,
  //置顶:1-是 0--否
  isTop, 
  //状态 1未发布 2立即发布
  status, 
  //	类型:1-文章 2-视频
  contentType,
  // 所属医生id
  docId
){
  return request({
    url: '/cms/api/v1/cms/content/updateContent',
    method: 'post',
    data: {
      id,
      isTop,
      status,
      contentType,
      docId
    }
  })
}
// 详情
export function getContentById(
  id
){
  return request({
    url: '/cms/api/v1/cms/content/getContentById',
    method: 'get',
    params: {
      id
    }
  })
}
// 查询 草稿 内容
export function queryDraftList(){
  return request({
    url: '/cms/api/v1/cms/content/queryDraftList',
    method: 'get',
    params: {
      
    }
  })
}
// 删除草稿
export function deleteDraft(
  id
){
  return request({
    url: '/cms/api/v1/cms/content/deleteDraft',
    method: 'post',
    data: {
      id
    }
  })
}
// 关联会议、
export function queryMeetingList(
  //标题
  title,
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/cms/api/v1/cms/content/queryMeetingPage',
    method: 'post',
    data: {
      title,
      current,
      size
    }
  })
}
// 关联医生
export function queryDoctorByNameAndHospital(
  //医生名称
  doctorName,
  //医院名称
  hospitalName,
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/cms/api/v1/cms/content/queryDoctorByNameAndHospital',
    method: 'post',
    data: {
      doctorName,
      hospitalName,
      current,
      size
    }
  })
}
// 医生端修改引用
export function updateReferStatus(
  //列表id
  id,
  // 引用状态 1-引用，2-取消引用
	referStatus
){
  return request({
    url: '/cms/api/v1/ihDoc/content/updateReferStatus',
    method: 'post',
    data: {
      id, 
      referStatus
    }
  })
}
//更新IHDoc文章权限
export function updateIHDocContentAuth(
  //列表id
  id,
  //内容权限 1-仅登录，2-需认证
	contentAuth
){
  return request({
    url: '/cms/api/v1/ihDoc/content/updateIHDocContentAuth',
    method: 'post',
    data: {
      id, 
      contentAuth
    }
  })
}
// 健康管理中心 修改引用
export function hmcUpdateReferStatus(
  //列表id
  id,
  // 引用状态 1-引用，2-取消引用
	referStatus
){
  return request({
    url: '/cms/api/v1/hmc/content/updateReferStatus',
    method: 'post',
    data: {
      id, 
      referStatus
    }
  })
}
// 内容问答管理
export function queryQaPage(
  //问答id
  id,
  //针对的内容
	title,
  //创建人类型 1-患者，2-医生
  userType,
  //上级问答id
  qaId,
  //业务线id 1-HMC,2-IH-doc,3-IH-patient
  lineId,
  //开始时间
  beginDate,
  //结束时间
  endDate,
  current,
  size
){
  return request({
    url: '/cms/api/v1/qa/queryQaPage',
    method: 'get',
    params: {
      id,
      title,
      userType,
      qaId,
      lineId,
      beginDate,
      endDate,
      current,
      size
    }
  })
}
//切换状态
export function switchShowStatus(
  //列表id
  id,
  //展示状态 1-展示，2-关闭
	showStatus
){
  return request({
    url: '/cms/api/v1/qa/switchShowStatus',
    method: 'post',
    data: {
      id, 
      showStatus
    }
  })
}