// 疑问处理
import request from '@/subject/admin/utils/request'
// 问题知识库列表
export function pageList(
  title, //标题
  startCreateTime, // 创建开始时间
  endCreateTime, // 创建结束时间
  current, //第几页，默认：1
	size // 每页记录数，默认：10
){
  return request({
    url: '/cms/api/v1/question/pageList',
    method: 'post',
    data: {
      title,
      startCreateTime,
      endCreateTime,
      current,
      size
    }
  })
}
// 问题知识库 编辑/添加
export function baseUpdate(
  categoryId, //	所属分类
	content, //内容详情
	documentIdList, //关联文献id集合
	standardInfoList, //关联药品
	title, //问题标题
  description, //描述
  id //ID
){
  return request({
    url: '/cms/api/v1/question/update',
    method: 'post',
    data: {
      categoryId,
      content,
      documentIdList,
      standardInfoList,
      title,
      description,
      id
    }
  })
}
// 获取以岭商品信息
export function standardGoods(
  name, //商品名称
  current,
	size
){
  return request({
    url: '/cms/api/v1/question/get/standardGoods',
    method: 'post',
    data: {
      name,
      current,
      size
    }
  })
}
// 获取以岭商品规格信息
export function specification(
  standardGoodId
){
  return request({
    url: '/cms/api/v1/question/get/specification',
    method: 'get',
    params: {
      standardGoodId
    }
  })
}
// 问题知识库详情
export function detail(
  questionId
){
  return request({
    url: '/cms/api/v1/question/detail',
    method: 'get',
    params: {
      questionId
    }
  })
}
// 问题知识库 删除
export function questionDelete(
  questionId
){
  return request({
    url: '/cms/api/v1/question/delete',
    method: 'get',
    params: {
      questionId
    }
  })
}
// 医代问题社区列表
export function communityList(
  startCreateTime, //创建开始时间
  endCreateTime, //	创建结束时间
  replyFlag, //是否回复 0-全部 1-未回复 2-已回复
  current, // 当前页，默认1
  size // 每页显示条数，默认10
){
  return request({
    url: '/cms/api/v1/question/delegate/list',
    method: 'post',
    data: {
      startCreateTime, 
      endCreateTime,
      replyFlag,
      current,
      size
    }
  })
}
// 医药代表社区保存回复
export function replyAdd(
  questionId, //疑问ID
	replyContent, //回复内容
	replyDocumentIdList, //关联的文献ID
	replyFileKeyList //关联文件key
){
  return request({
    url: '/cms/api/v1/question/reply/add',
    method: 'post',
    data: {
      questionId,
      replyContent,
      replyDocumentIdList,
      replyFileKeyList
    }
  })
}
// 医药代表社区详情
export function delegateDetail(
  questionId //疑问ID
){
  return request({
    url: '/cms/api/v1/question/delegate/detail',
    method: 'get',
    params: {
      questionId
    }
  })
}