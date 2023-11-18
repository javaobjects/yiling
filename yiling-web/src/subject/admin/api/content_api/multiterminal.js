//医生端以及健康管理中心接口
import request from '@/subject/admin/utils/request'
//健康管理中心数据列表
export function queryContentPage(
  //标题
  title, 
  //所属板块
  moduleId,
  //所属栏目
  categoryId,
  //内容类别
  contentType,
  //是否手动排序 0-否，1-是
  handRankFlag,
  //医生id
  docId,
  //状态：1-未发布 2-已发布
  status,
  //开始创建时间
  startTime,
  //结束创建时间
  endTime,
  //创建来源 1-运营后台, 2-IH后台
  createSource,
  // 置顶:1-是 0--否
  topFlag,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/cms/api/v1/hmc/content/queryContentPage',
    method: 'post',
    data: {
      title, 
      moduleId,
      categoryId,
      contentType,
      handRankFlag,
      docId,
      status,
      startTime,
      endTime,
      createSource,
      topFlag,
      current,
      size 
    }
  })
}
//健康管理中心内容排序与置顶
export function contentRank(
  //栏目排序
  categoryRank,
  //id
	id,
  //是否置顶 0-否，1-是
	topFlag
){
  return request({
    url: '/cms/api/v1/hmc/content/contentRank',
    method: 'post',
    data: {
      categoryRank,
      id,
      topFlag
    }
  })
}
// 健康管理中心添加引用
export function addContent(
  list
){
  return request({
    url: '/cms/api/v1/hmc/content/addContent',
    method: 'post',
    data: {
      list
    }
  })
}
//医生端数据列表
export function doctorContentPage(
  //标题
  title, 
  //所属板块
  moduleId,
  //所属栏目
  categoryId,
  //内容类别
  contentType,
  //是否手动排序 0-否，1-是
  handRankFlag,
  //医生id
  docId,
  //状态：1-未发布 2-已发布
  status,
  //开始创建时间
  startTime,
  //结束创建时间
  endTime,
  //创建来源 1-运营后台, 2-IH后台
  createSource,
  // 置顶:1-是 0--否
  topFlag,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/cms/api/v1/ihDoc/content/queryContentPage',
    method: 'post',
    data: {
      title, 
      moduleId,
      categoryId,
      contentType,
      handRankFlag,
      docId,
      status,
      startTime,
      endTime,
      createSource,
      topFlag,
      current,
      size 
    }
  })
}
// 医生端 - 排序
export function doctorContentRank(
  //栏目排序
  categoryRank,
  //id
	id,
  //是否置顶 0-否，1-是
	topFlag
){
  return request({
    url: '/cms/api/v1/ihDoc/content/contentRank',
    method: 'post',
    data: {
      categoryRank,
      id,
      topFlag
    }
  })
}
// 获取 所属板块 以及所属板块下的栏目数据
export function getModulesByLineId(
  //业务线id
  lineId
){
  return request({
    url: '/cms/api/v1/cms/category/getModulesByLineId',
    method: 'get',
    params: {
      lineId
    }
  })
}
// 医生端-添加引用
export function doctorContent(
  //业务引用数组
  contentList
){
  return request({
    url: '/cms/api/v1/ihDoc/content/addContent',
    method: 'post',
    data: {
      contentList
    }
  })
  
}

// 销售助手
//列表
export function zsQueryContentPage(
  //标题
  title, 
  //所属板块
  moduleId,
  //所属栏目
  categoryId,
  //内容类别
  contentType,
  //是否手动排序 0-否，1-是
  handRankFlag,
  //医生id
  docId,
  //状态：1-未发布 2-已发布
  status,
  //开始创建时间
  startTime,
  //结束创建时间
  endTime,
  //创建来源 1-运营后台, 2-IH后台
  createSource,
  // 置顶:1-是 0--否
  topFlag,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/cms/api/v1/sa/content/queryContentPage',
    method: 'post',
    data: {
      title, 
      moduleId,
      categoryId,
      contentType,
      handRankFlag,
      docId,
      status,
      startTime,
      endTime,
      createSource,
      topFlag,
      current,
      size 
    }
  })
}
//销售助手 添加引用保存
export function zsAddContent(
  list
){
  return request({
    url: '/cms/api/v1/sa/content/addContent',
    method: 'post',
    data: {
      list
    }
  })
}
//销售助手 修改引用
export function zsUpdateReferStatus(
  //列表id
  id,
  // 引用状态 1-引用，2-取消引用
	referStatus
){
  return request({
    url: '/cms/api/v1/sa/content/updateReferStatus',
    method: 'post',
    data: {
      id, 
      referStatus
    }
  })
}
// 销售助手 - 排序
export function zsContentRank(
  //栏目排序
  categoryRank,
  //id
	id,
  //是否置顶 0-否，1-是
	topFlag
){
  return request({
    url: '/cms/api/v1/sa/content/contentRank',
    method: 'post',
    data: {
      categoryRank,
      id,
      topFlag
    }
  })
}

//大运河
//列表
export function dyhQueryContentPage(
  //标题
  title, 
  //所属板块
  moduleId,
  //所属栏目
  categoryId,
  //内容类别
  contentType,
  //是否手动排序 0-否，1-是
  handRankFlag,
  //医生id
  docId,
  //状态：1-未发布 2-已发布
  status,
  //开始创建时间
  startTime,
  //结束创建时间
  endTime,
  //创建来源 1-运营后台, 2-IH后台
  createSource,
  // 置顶:1-是 0--否
  topFlag,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/cms/api/v1/b2b/content/queryContentPage',
    method: 'post',
    data: {
      title, 
      moduleId,
      categoryId,
      contentType,
      handRankFlag,
      docId,
      status,
      startTime,
      endTime,
      createSource,
      topFlag,
      current,
      size 
    }
  })
}
//销售助手 添加引用保存
export function dyhAddContent(
  list
){
  return request({
    url: '/cms/api/v1/b2b/content/addContent',
    method: 'post',
    data: {
      list
    }
  })
}
//大运河 修改引用
export function dyhUpdateReferStatus(
  //列表id
  id,
  // 引用状态 1-引用，2-取消引用
	referStatus
){
  return request({
    url: '/cms/api/v1/b2b/content/updateReferStatus',
    method: 'post',
    data: {
      id, 
      referStatus
    }
  })
}
//大运河 - 排序
export function dyhContentRank(
  //栏目排序
  categoryRank,
  //id
	id,
  //是否置顶 0-否，1-是
	topFlag
){
  return request({
    url: '/cms/api/v1/b2b/content/contentRank',
    method: 'post',
    data: {
      categoryRank,
      id,
      topFlag
    }
  })
}