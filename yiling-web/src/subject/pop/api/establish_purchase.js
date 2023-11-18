import request from '@/subject/pop/utils/request'

// 查询采购关系列表
export function queryProcRelationPage(
  current,
  size,
  // 采购关系编号
  procRelationNumber,
  // 渠道商eid
  channelPartnerEid,
  // 渠道类型
  channelId,
  // 配送类型：1-工业直配 2-三方配送
  deliveryType,
  // 工业主体eid
  factoryEid,
  // 配送商eid
  deliveryEid,
  // 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
  procRelationStatus
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryProcRelationPage',
    method: 'post',
    data: {
      current,
      size,
      procRelationNumber,
      channelPartnerEid,
      channelId,
      deliveryType,
      factoryEid,
      deliveryEid,
      procRelationStatus
    }
  })
}
// 查询渠道商列表
export function queryChannelPartnerPage(
  current,
  size,
  // 客户名称
  name
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryChannelPartnerPage',
    method: 'post',
    data: {
      current,
      size,
      name
    }
  })
}
// 查询配送商列表
export function queryDeliveryPartnerPage(
  current,
  size,
  // 客户名称
  name
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryDeliveryPartnerPage',
    method: 'post',
    data: {
      current,
      size,
      name
    }
  })
}
// 查询工业主体
export function queryProcurementRelationMainPart() {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/mainPart/list',
    method: 'get',
    params: {}
  })
}
// 建采管理-新增-保存基本信息
export function querySaveProcurementRelation(
  // 渠道商eid
  channelPartnerEid,
  // 渠道商名称
  channelPartnerName,
  // 配送类型：1-工业直配 2-三方配送
  deliveryType,
  // 工业主体eid
  factoryEid,
  // 工业主体名称
  factoryName,
  // 配送商eid
  deliveryEid,
  // 配送商名称
  deliveryName,
  startTime,
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/saveProcurementRelation',
    method: 'post',
    data: {
      channelPartnerEid,
      channelPartnerName,
      deliveryType,
      factoryEid,
      factoryName,
      deliveryEid,
      deliveryName,
      startTime,
      endTime
    }
  })
}
// 建采管理-删除
export function queryDeleteProcRelationById(
  relationId
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/deleteProcRelationById',
    method: 'post',
    data: {
      relationId
    }
  })
}
// 建采管理-停用
export function queryCloseProcRelationById(
  // id
  relationId
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/closeProcRelationById',
    method: 'post',
    data: {
      relationId
    }
  })
}
// 建采管理-根据采购关系id查询可采商品列表
export function queryProcRelationGoodsOptionalPage(
  current,
  size,
  // 采购关系id
  procRelationId,
  // 商品名称
  name,
  // 批准文号
  licenseNo,
  // 专利类型 0-全部 1-非专利 2-专利
  isPatent
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryProcRelationGoodsOptionalPage',
    method: 'post',
    data: {
      current,
      size,
      procRelationId,
      name,
      licenseNo,
      isPatent
    }
  })
}
// 建采管理-根据采购关系id查询可采全部商品
export function queryFactoryGoodsList(
  // 采购关系id
  procRelationId,
  // 商品名称
  name,
  // 批准文号
  licenseNo,
  // 专利类型 0-全部 1-非专利 2-专利
  isPatent
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryFactoryGoodsList',
    method: 'post',
    data: {
      procRelationId,
      name,
      licenseNo,
      isPatent
    }
  })
}

// 建采管理-保存采购关系
export function querySaveProcRelationGoods(
  // 采购关系id
  relationId,
  // 商品列表
  goodsInfoList
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/saveProcRelationGoods',
    method: 'post',
    data: {
      relationId,
      goodsInfoList
    }
  })
}
// 建采管理-获取列表项详情
export function queryProcRelationDetail(
  // 采购关系id
  relationId
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryProcRelationDetail',
    method: 'post',
    data: {
      relationId
    }
  })
}
// 建采管理-详情-修改记录
export function queryProcRelationSnapshotList(
  size,
  current,
  // 采购关系id
  relationId
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryProcRelationSnapshotList',
    method: 'post',
    data: {
      size,
      current,
      relationId
    }
  })
}
// 建采管理-详情-修改记录-详情
export function queryProcRelationGoodsSnapshotDetail(
  versionId
) {
  return request({
    url: '/admin/pop/api/v1/procurementRelation/queryProcRelationGoodsSnapshotDetail',
    method: 'post',
    data: {
      versionId
    }
  })
}

// 商品模板
//列表
export function queryTemplatePage(
  //模板编号
  templateNumber,
  //模板名称
  templateName,
  //操作人
  operationUser,
  current,
  size
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/queryTemplatePage',
    method: 'post',
    data: {
      templateNumber,
      templateName,
      operationUser,
      current,
      size
    }
  })
}
//保存或更新模板
export function saveTemplate(
  //模板id
  id,
  //模板名称
  templateName
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/saveTemplate',
    method: 'post',
    data: {
      id,
      templateName
    }
  })
}
//查询工业主体
export function mainPartList() {
  return request({
    url: '/admin/pop/api/v1/procurementImport/mainPart/list',
    method: 'get',
    params: {
    }
  });
}
//查詢模板下的商品列表
export function queryTemplateGoodsList(
  //模板id
  templateId,
  //工业eid
  factoryEid
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/queryTemplateGoodsList',
    method: 'post',
    data: {
      templateId,
      factoryEid
    }
  })
}
//根据采购模板id查询可采商品列表
export function queryProcTempalteGoodsOptionalPage(
  //模板id
  templateId,
  //工业eid
  factoryEid,
  //商品名称
  name,
  //批准文号
  licenseNo,
  //专利类型
  isPatent,
  current,
  size
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/queryProcTempalteGoodsOptionalPage',
    method: 'post',
    data: {
      templateId,
      factoryEid,
      name,
      licenseNo,
      isPatent,
      current,
      size
    }
  })
}
//向模板中添加商品
export function addGoodsToTemplate(
  //模板id
  templateId,
  //工业eid
  factoryEid,
  isPatent,
  goodsName,
  sellSpecifications,
  sellSpecificationsId,
  licenseNo,
  standardId,
  goodsId
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/addGoodsToTemplate',
    method: 'post',
    data: {
      templateId,
      factoryEid,
      isPatent,
      goodsName,
      sellSpecifications,
      sellSpecificationsId,
      licenseNo,
      standardId,
      goodsId
    }
  })
}
//根据采购模板id添加全部可采商品列表
export function addAllGoodsToTemplate(
  //模板id
  templateId,
  //工业eid
  factoryEid,
  isPatent,
  licenseNo,
  name
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/addAllGoodsToTemplate',
    method: 'post',
    data: {
      templateId,
      factoryEid,
      isPatent,
      licenseNo,
      name
    }
  })
}
//批量设置优惠
export function updateTemplateGoodsRebate(
  //id列表
  idList,
  //商品优惠折扣
  rebate
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/updateTemplateGoodsRebate',
    method: 'post',
    data: {
      idList,
      rebate
    }
  })
}
//从模板中移除商品
export function deleteTemplateGoods(
  //商品id
  id
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/deleteTemplateGoods',
    method: 'post',
    data: {
      id
    }
  })
}
//删除模板
export function deleteTemplate(
  //模板id
  id
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/deleteTemplate',
    method: 'post',
    data: {
      id
    }
  })
}
//查询模板信息
export function queryTemplateInfo(
  id
) {
  return request({
    url: '/admin/pop/api/v1/procurementImport/queryTemplateInfo',
    method: 'get',
    params: {
      id
    }
  })
}