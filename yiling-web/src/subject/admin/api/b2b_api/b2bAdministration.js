// 金刚位 管理接口
import request from '@/subject/admin/utils/request'
// 获取企业标签选项列表
// export function options(){
//   return request({
//     url: '/dataCenter/api/v1/enterprise/tag/options',
//     method: 'get',
//     params: {}
//   })
// }
// 分页列表查询金刚位-运营后台
export function pageList(
  current, //第几页，默认：1
	size, //每页记录数，默认：10
	title, //金刚位标题
	vajraStatus //	状态：0-全部 1-启用 2-停用
){
  return request({
    url: '/b2b/api/v1/vajra/pageList',
    method: 'post',
    data: {
      current,
      size,
      title,
      vajraStatus
    }
  })
}
// 新增和编辑金刚位信息
export function save(
  activityLinks, //活动详情超链接
	eid, //企业ID 
	goodsId, //商品页商品ID
	id, //	id
	linkType, //页面配置1-活动详情H5 2-全部分类 3-搜索结果页 4-商品页 5-店铺页 6-分类页	
	pic, //	金刚位图片地址
	
	searchKeywords, //搜索结果页关键
	sellerEid, //店铺页跟供应商ID
	sort, //排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序
	title, //金刚位标题名称
	vajraStatus //状态：0-启用 1-停用
){
  return request({
    url: '/b2b/api/v1/vajra/save',
    method: 'post',
    data: {
      activityLinks,
      eid,
      goodsId,
      id,
      linkType,
      pic,
    
      searchKeywords,
      sellerEid,
      sort,
      title,
      vajraStatus
    }
  })
}
// 根据id查询金刚位 信息
export function getById(
  id
){
  return request({
    url: '/b2b/api/v1/vajra/getById',
    method: 'get',
    params: {
      id
    }
  })
}
//金刚位 启用禁用
export function editStatus(
  id,
	vajraStatus //1-启用 2-停用
){
  return request({
    url: '/b2b/api/v1/vajra/editStatus',
    method: 'post',
    data: {
      id,
      vajraStatus 
    }
  })
}
// 金刚位删除
export function dataDelete(
  id
){
  return request({
    url: '/b2b/api/v1/vajra/delete',
    method: 'post',
    data: {
      id
    }
  })
}
// 金刚位排序，权重数字修改
export function editWeight(
  id,
  sort //排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序	
){
  return request({
    url: '/b2b/api/v1/vajra/editWeight',
    method: 'post',
    data: {
      id,
      sort
    }
  })
}

// banner

//分页列表查询B2B中banner-运营后台
export function bannerPageList(
  bannerStatus, //状态：0-全部 1-启用 2-停用
	createEndTime, //创建截止时间
	createStartTime, //创建起始时间
	current, //第几页，默认：1
	size, //每页记录数，默认：10
	title, //banner标题
  usageScenario, //使用位置：0-所有 1-B2B移动端主Banner 2-B2B移动端副Banner
	useEndTime, //投放截止时间
	useStartTime //投放起始时间
){
  return request({
    url: '/b2b/api/v1/banner/pageList',
    method: 'post',
    data: {
      bannerStatus, 
      createEndTime,
      createStartTime, 
      current, 
      size, 
      title,
      usageScenario,
      useEndTime,
      useStartTime 
    }
  })
}
// B2B新增和编辑banner信息
export function bannerSave(
  //活动详情超链接
  activityLinks,
  //显示状态：1-启用 2-停用
	bannerStatus,
  //企业ID
	eid,
  //商品页商品ID
	goodsId,
  //搜索结果页厂家
	goodsManufacturer,
	id,
  //页面配置1-活动详情H5 2-全部分类 3-搜索结果页 4-商品页 5-旗舰店页 6-分类页
	linkType,
  //banner图片地址
	pic,
  //搜索结果页关键词
	searchKeywords,
  //旗舰店页的供应商ID
	sellerEid,
  //排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序	
	sort,
  //有效起始时间
	startTime,
  //有效结束时间
	stopTime,
  //banner标题
	title,
  //使用位置：1-B2B移动端主Banner 2-B2B移动端副Banner
	usageScenario,
  //投放店铺
  bannerEnterpriseList
){
  return request({
    url: '/b2b/api/v1/banner/save',
    method: 'post',
    data: {
      activityLinks,
      bannerStatus,
      eid,
      goodsId,
      goodsManufacturer,
      id,
      linkType,
      pic,
      searchKeywords,
      sellerEid,
      sort,
      startTime,
      stopTime,
      title,
      usageScenario,
      bannerEnterpriseList
    }
  })
}
// 启用停用
export function bannerEditStatus(
	bannerStatus, //状态：1-启用 2-停用
	id
){
  return request({
    url: '/b2b/api/v1/banner/editStatus',
    method: 'post',
    data: {
      bannerStatus,
      id
    }
  })
}
// B2B中banner排序，权重数字修改
export function bannerEditWeight(
	id,
	sort //排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序	
){
  return request({
    url: '/b2b/api/v1/banner/editWeight',
    method: 'post',
    data: {
      id,sort
    }
  })
}
// 删除B2B中banner
export function bannerDelete(
	id
){
  return request({
    url: '/b2b/api/v1/banner/delete',
    method: 'post',
    data: {
      id
    }
  })
}
// 通过id查询banner
export function bannerGetById(
  id
){
  return request({
    url: '/b2b/api/v1/banner/getById',
    method: 'get',
    params: {
      id
    }
  })
}
// 热词管理

// B2B新增和编辑热词
export function hotWordsSave(
  content, //热词名称
	id,
	sort, //排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序	
	startTime, //	开始时间
	stopTime, // 结束时间
	useStatus //启用状态：0-启用 1-停用
){
  return request({
    url: '/b2b/api/v1/hotWords/save',
    method: 'post',
    data: {
      content,
      id,
      sort,
      startTime,
      stopTime,
      useStatus
    }
  })
}
// B2B中热词的停用
export function hotWordsEditStatus(
  id,
	useStatus //	状态：1-启用 2-停用
){
  return request({
    url: '/b2b/api/v1/hotWords/editStatus',
    method: 'post',
    data: {
      id,useStatus
    }
  })
}
// B2B中热词排序，权重数字修改
export function hotWordsEditWeight(
  id,
	sort //排序,排序逻辑 1-200，200权重最高，权重一样时候，将按照时间排序	
){
  return request({
    url: '/b2b/api/v1/hotWords/editWeight',
    method: 'post',
    data: {
      id,sort
    }
  })
}
// 分页列表查询B2B中热词-运营后台
export function hotWordsPageList(
  content, //	热词名称
	createEndTime, //创建截止时间
	createStartTime, //创建起始时间
	current, //第几页，默认：1
	size, //每页记录数，默认：10
	useEndTime, //投放截止时间
	useStartTime, //投放起始时间
	useStatus //状态：0-全部 1-启用 2-停用
){
  return request({
    url: '/b2b/api/v1/hotWords/pageList',
    method: 'post',
    data: {
      content,
      createEndTime,
      createStartTime,
      current,
      size,
      useEndTime,
      useStartTime,
      useStatus
    }
  })
}
// 编辑时查询B2B中热词-运营后台
export function bannerQuery(
  id
){
  return request({
    url: '/b2b/api/v1/hotWords/query',
    method: 'get',
    params: {
      id
    }
  })
}
//配置投放店铺
export function queryEnterpriseListPage(
  //企业id
  eid,
  //企业名称
  ename,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size
){
  return request({
    url: '/b2b/api/v1/couponActivity/queryEnterpriseListPage',
    method: 'post',
    data: {
      eid,
      ename,
      current,
      size
    }
  })
}

//开屏位
//分页查询开屏位
export function queryListPage(
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size
){
  return request({
    url: '/b2b/api/v1/openPosition/queryListPage',
    method: 'post',
    data: {
      current,
      size
    }
  })
}
//保存/编辑开屏位
export function saveOpenPosition(
  //ID（新增不传入，编辑必传）
  id,
  //标题
  title,
  //配置链接
  link,
  //发布状态：1-暂不发布 2-立即发布
  status,
  //图片
  picture
){
  return request({
    url: '/b2b/api/v1/openPosition/saveOpenPosition',
    method: 'post',
    data: {
      id,
      title,
      link,
      status,
      picture
    }
  })
}
//详情
export function homeById(
  id
){
  return request({
    url: '/b2b/api/v1/openPosition/getById',
    method: 'get',
    params: {
      id
    }
  })
}
//更新状态
export function updateStatus(
  //ID（新增不传入，编辑必传）
  id,
  //发布状态：1-暂不发布 2-立即发布
  status
){
  return request({
    url: '/b2b/api/v1/openPosition/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}
//删除开屏位
export function deleteOpenPosition(
  id
){
  return request({
    url: '/b2b/api/v1/openPosition/deleteOpenPosition',
    method: 'get',
    params: {
      id
    }
  })
}