// 文章管理接口
import request from '@/subject/admin/utils/request'
export function pageList(
  articleStatus, //文章状态
	articleTitle, //标题
	current, // 第几页，默认：1
	size //每页记录数，默认：10
){
  return request({
    url: '/dataCenter/api/v1/article/pageList',
    method: 'post',
    data: {
      articleStatus,
      articleTitle,
      current,
      size
    }
    
  })
}
// 保存文章
export function saveOrUpdate(
  articleContent, //文章内容	
	articleDesc, //文章描述
	articleStatus, //文章状态 1-可用，2-停用
	articleTitle, //文章标题
	id //id
){
  return request({
    url: '/dataCenter/api/v1/article/saveOrUpdate',
    method: 'post',
    data: {
      articleContent,
      articleDesc,
      articleStatus,
      articleTitle,
      id
    }
    
  })
}
// 文章详情
export function queryById(
	id //id
){
  return request({
    url: '/dataCenter/api/v1/article/queryById',
    method: 'post',
    data: {
      id
    }
    
  })
}
// 启用禁用
export function switchStatus(
  articleStatus, //1启用 2 禁用
	id //id
){
  return request({
    url: '/dataCenter/api/v1/article/switchStatus',
    method: 'post',
    data: {
      articleStatus,
      id
    }
    
  })
}