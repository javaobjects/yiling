// 前台分类设置
import request from '@/subject/admin/utils/request'

// 添加
export function addContent(
  //业务线id
  lineId,
  //模块id
  moduleId,
  //栏目名称
  categoryName
){
  return request({
    url: '/cms/api/v1/cms/category/addCategory',
    method: 'post',
    data: {
      lineId,
      moduleId,
      categoryName
    }
  })
}
//列表
export function queryCategoryList(
  status
){
  return request({
    url: '/cms/api/v1/cms/category/queryCategoryList',
    method: 'get',
    params: {
      status
    }
  })
}
//编辑
export function updateCategory(
  //业务线id
  lineId,
  //模块id
  moduleId,
  //栏目名称
  categoryName,
	id
){
  return request({
    url: '/cms/api/v1/cms/category/updateCategory',
    method: 'post',
    data: {
      lineId,
      moduleId,
      categoryName,
      id
    }
  })
}
//排序
export function querySort(
  //业务线id
  lineId,
  //模块id
  moduleId,
  //排序
  categorySort,
  id
){
  return request({
    url: '/cms/api/v1/cms/category/updateCategory',
    method: 'post',
    data: {
      lineId,
      moduleId,
      categorySort,
      id
    }
  })
}
//启用禁用
export function statusType(
  //业务线id
  lineId,
  //模块id
  moduleId,
  //id
  id,
  //0-禁用 1启用
  status
){
  return request({
    url: '/cms/api/v1/cms/category/updateCategory',
    method: 'post',
    data: {
      lineId,
      moduleId,
      id,
      status
    }
  })
}
