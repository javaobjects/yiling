// 文献管理
import request from '@/subject/admin/utils/request'

// 文献管理列表
export function queryContentPage(
  current, //第几页，默认：1
  startTime, //开始时间
  endTime, //结束时间
  title, //标题
  size, //每页记录数，默认：10
  status // 状态 1未发布 2发布
){
  return request({
    url: '/cms/api/v1/document/queryContentPage',
    method: 'get',
    params: {
      current,
      startTime,
      endTime,
      title,
      size,
      status
    }
  })
}
// 添加
export function addContent(
  author, //作者
	categoryId, //文献栏目id
	content, //内容概述	
	displayLines, //显示业务线	
	documentFileUrl, //文献pdf oss key
  documentFileName, //文献名称
	isOpen, // 是否公开：0-否 1-是
	resume, //简述	
	source, //来源
	standardGoodsIdList, //药品
	status, //状态：1-未发布 2-已发布
	title //标题
){
  return request({
    url: '/cms/api/v1/document/addContent',
    method: 'post',
    data: {
      author,
      categoryId,
      content,
      displayLines,
      documentFileUrl,
      documentFileName,
      isOpen,
      resume,
      source,
      standardGoodsIdList,
      status,
      title
    }
  })
}
// 详情
export function getDocumentById(
  id
){
  return request({
    url: '/cms/api/v1/document/getDocumentById',
    method: 'get',
    params: {
      id
    }
  })
}
// 编辑
export function updateContent(
  author, //作者
	categoryId, //文献栏目id
	content, //内容概述	
	displayLines, //显示业务线	
	documentFileUrl, //文献pdf oss key
  documentFileName, //文献名称
	isOpen, // 是否公开：0-否 1-是
	resume, //简述	
	source, //来源
	standardGoodsIdList, //药品
	status, //状态：1-未发布 2-已发布
	title, //标题
  id
){
  return request({
    url: '/cms/api/v1/document/updateContent',
    method: 'post',
    data: {
      author,
      categoryId,
      content,
      displayLines,
      documentFileUrl,
      documentFileName,
      isOpen,
      resume,
      source,
      standardGoodsIdList,
      status,
      title,
      id
    }
  })
}
// 是否置顶 Topping
export function isTopping(
  id,
  status
){
  return request({
    url: '/cms/api/v1/document/updateContent',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 栏目设置

// 栏目列表
export function queryCategoryList(
){
  return request({
    url: '/cms/api/v1/document/category/queryCategoryList',
    method: 'get',
    params: {
      
    }
  })
}
// 添加栏目
export function addCategory(
  categoryName //	栏目名称
){
  return request({
    url: '/cms/api/v1/document/category/addCategory',
    method: 'post',
    data: {
      categoryName
    }
  })
}
// 编辑和禁用启用
export function updateCategory(
  categoryName, //	栏目名称
  id,
	status //0-禁用 1启用
){
  return request({
    url: '/cms/api/v1/document/category/updateCategory',
    method: 'post',
    data: {
      categoryName,
      id,
      status
    }
  })
}
