import request from '@/subject/admin/utils/request';

// 获取基础药品管理，标准商品列表信息
export function getStandardProductsList(
  current,
  size,
  // 管制类型 管制类型：0-非管制 1-管制
  controlType,
  // 商品类别 商品类别：1-普通药品 2-中药饮片 3-中药材 4-消杀 5-保健食品 6-食品 7-医疗器械
  goodsType,
  // 是否医保：1-是 2-非 3-未采集到相关信息
  isYb,
  // 注册证号
  licenseNo,
  // 生产厂家
  manufacturer,
  // 生产地址
  manufacturerAddress,
  // 商品名称
  name,
  // 处方类型：1-处方药 2-甲类非处方药 3-乙类非处方药 4-其他
  otcType,
  // 有无图片：1-有 2-无
  pictureFlag,
  // 特殊成分：0-不含麻黄碱 1-含麻黄碱
  specialComposition,
  // 标准库一级分类id
  standardCategoryId1,
  // 标准库二级分类id
  standardCategoryId2,
  // 标准库规格id
  standardId,
  // 标签ID列表
  tagIds
) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/pageList',
    method: 'post',
    data: {
      current,
      size,
      controlType,
      goodsType,
      isYb,
      licenseNo,
      manufacturer,
      manufacturerAddress,
      name,
      otcType,
      pictureFlag,
      specialComposition,
      standardCategoryId1,
      standardCategoryId2,
      standardId,
      tagIds
    }
  });
}

// 根据商品id,获取商品规格列表
export function getGoodsSpecificationById(id) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/get/specification',
    method: 'get',
    params: {
      id
    }
  });
}
// 获取基础药品详情
export function getGoodsDetailById(id) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/get',
    method: 'get',
    params: {
      id
    }
  });
}
// 获取跳转新增
export function getJumpGoodsDetailById(id) {
  return request({
    url: '/dataCenter/api/v1/goods/audit/jumpStandardGoods',
    method: 'get',
    params: {
      id
    }
  });
}
// 编辑基础药品信息
export function editGoodsDetail(
  // 基础信息和其他信息
  baseInfo,
  // 中药饮片说明书信息
  decoctionInstructionsInfo,
  // 消杀品说明书信息
  disinfectionInstructionsInfo,
  // 食品说明书信息
  foodsInstructionsInfo,
  // 药品说明书信息
  goodsInstructionsInfo,
  // 保健食品说明书信息
  healthInstructionsInfo,
  // 中药材说明书信息
  materialsInstructionsInfo,
  // 医疗器械说明书
  medicalInstrumentInfo,
  // 中药饮片说明书信息
  dispensingGranuleInfo,
  // 图片信息
  picBasicsInfoList,
  // 规格图片信息	
  specificationInfo
) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/save',
    method: 'post',
    data: {
      baseInfo,
      decoctionInstructionsInfo,
      disinfectionInstructionsInfo,
      foodsInstructionsInfo,
      goodsInstructionsInfo,
      healthInstructionsInfo,
      materialsInstructionsInfo,
      medicalInstrumentInfo,
      dispensingGranuleInfo,
      picBasicsInfoList,
      specificationInfo
    }
  });
}

// 查询药品分类
export function getGoodsClassCate() {
  return request({
    url: '/dataCenter/api/v1/standard/goods/category/getAll',
    method: 'post',
    data: {}
  });
}
// 编辑药品分类名称
export function editCateInfo(id, name) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/category/update/name',
    method: 'post',
    data: {
      id,
      name
    }
  });
}
// 编辑 药品分类
export function saveStandardGoodsCate(name, parentId) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/category/save',
    method: 'post',
    data: {
      name,
      parentId
    }
  });
}
// 二级药品分类 归类接口
export function updateTypeStandardGoodsCate(id, parentId) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/category/update/type',
    method: 'get',
    params: {
      id,
      parentId
    }
  });
}

// 商品对应标准库数据列表
export function goodsToStandardLibraryList(
  current,
  ename,
  licenseNo,
  manufacturer,
  name,
  size,
  source
) {
  return request({
    url: '/dataCenter/api/v1/goods/audit/list',
    method: 'post',
    data: {
      current,
      ename,
      licenseNo,
      manufacturer,
      name,
      size,
      source
    }
  });
}

//  商品对应标准库查询匹配状态  管理后台商品审核匹配
export function goodsToStandardLibraryStatus(id) {
  return request({
    url: '/dataCenter/api/v1/goods/audit/match',
    method: 'post',
    data: {
      id
    }
  });
}

// 商品对应标准库匹配弹窗列表
export function goodsToStandardLibraryDialogList(
  current,
  // 主键id
  id,
  licenseNo,
  manufacturer,
  name,
  size
) {
  return request({
    url: '/dataCenter/api/v1/goods/audit/standardGoodsPage',
    method: 'post',
    data: {
      current,
      id,
      licenseNo,
      manufacturer,
      name,
      size
    }
  });
}
// 商品对应标准库匹配 规格弹窗列表
export function goodsToStandardLibraryDialogSpecList(
  current,
  // 主键id
  id,
  size,
  standardId
) {
  return request({
    url: '/dataCenter/api/v1/goods/audit/standardSpecificationPage',
    method: 'post',
    data: {
      current,
      id,
      size,
      standardId
    }
  });
}
// 商品对应标准库匹配 规格弹窗列表
export function saveSellSpecifications(
  // 主键id
  id,
  // 选中的规格id
  sellSpecificationsId,
  // 匹配的商品库id
  standardId
) {
  return request({
    url: '/dataCenter/api/v1/goods/audit/saveSellSpecifications',
    method: 'post',
    data: {
      id,
      sellSpecificationsId,
      standardId
    }
  });
}
// 商品对应标准库， 驳回接口
export function rejectGoodsToStandardLibrary(
  // 主键id
  id,
  rejectMessage
) {
  return request({
    url: '/dataCenter/api/v1/goods/audit/reject',
    method: 'post',
    data: {
      id,
      rejectMessage
    }
  });
}
// 商品对应标准库， 确认入库并绑定心规格单位等
export function saveSpecificationAndGoLibrary(
  // 规格
  sellSpecifications,
  // 标准库id
  standardId,
  // 单位
  unit,
  // 条形码
  barcode
) {
  return request({
    url: '/dataCenter/api/v1/standard/goods/saveSpecification',
    method: 'post',
    data: {
      sellSpecifications,
      standardId,
      unit,
      barcode
    }
  });
}

// 查询分类树
export function getProductCategory() {
  return request({
    url: '/dataCenter/api/v1/standard/goods/category/getAll',
    method: 'post',
    data: {}
  });
}

// 查询标签列表
export function queryTagsListPage(
  //第几页，默认：1
  current, 
  //名称
	name, 
  //每页记录数，默认：10
	size 
){
  return request({
    url: '/dataCenter/api/v1/standard/tag/queryTagsListPage',
    method: 'post',
    data: {
      current, 
      name,
      size 
    }
  })
}
//删除标签
export function batchDeleteTags(
  //id数组
  tagsIdList 
){
  return request({
    url: '/dataCenter/api/v1/standard/tag/batchDeleteTags',
    method: 'post',
    data: {
      tagsIdList
    }
  })
}
// 新增标签
export function createTags(
  //描述
  description, 
  //名称
	name, 
  //类型：1-手动标签 2-自动标签
	type 
){
  return request({
    url: '/dataCenter/api/v1/standard/tag/createTags',
    method: 'post',
    data: {
      description, 
      name,
      type
    }
  })
}
// 修改标签
export function updateTags(
  //描述
  description, 
  id, 
  //名称
	name,
  //类型：1-手动标签 2-自动标签
	type 
){
  return request({
    url: '/dataCenter/api/v1/standard/tag/updateTags',
    method: 'post',
    data: {
      description, 
      id,
      name,
      type
    }
  })
}

// 获取标签列表
export function listByEid(standardId){
  return request({
    url: '/dataCenter/api/v1/standard/tag/listByEid',
    method: 'get',
    params: {
      //标准库ID
      standardId
    }
  })
}

// 保存单个标签信息
export function saveEnterpriseTags(standardId,tagIds){
  return request({
    url: '/dataCenter/api/v1/standard/tag/saveStandardGoodsTags',
    method: 'post',
    data: {
      //标准库ID
      standardId,
      //标签id数组
      tagIds 
    }
  })
}

// 获取标签选项列表
export function options(){
  return request({
    url: '/dataCenter/api/v1/standard/tag/options',
    method: 'get',
    params: {}
  })
}