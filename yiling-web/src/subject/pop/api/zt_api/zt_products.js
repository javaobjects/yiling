import request from '@/subject/pop/utils/request';

// 供应商详取商品列表
export function getZtProductList(
  current,
  goodsLines,
  auditStatus,
  licenseNo,
  manufacturer,
  name,
  size,
  // 商品编码
  goodsId,
  // 外部编码
  inSnOrSn
) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/list',
    method: 'post',
    data: {
      current,
      goodsLines,
      auditStatus,
      licenseNo,
      manufacturer,
      name,
      size,
      goodsId,
      inSnOrSn
    }
  });
}

// 商家后台-中台-商品管理-商品列表-获取未入库类型列表
export function getZtUnStorageList(
  current,
  size,
  // 商品编码
  goodsId,
  // 商品名称
  name,
  // 批文许可
  licenseNo,
  // 生产厂家
  manufacturer,
  // 外部编码
  inSnOrSn,
  // 供货渠道
  goodsLines,
  // erp商品标识：1-未入库
  erpFlag
) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/unStorageList',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      name,
      licenseNo,
      manufacturer,
      inSnOrSn,
      goodsLines,
      erpFlag
    }
  });
}

// 商家后台-中台-商品管理-商品列表-获取导出记录列表
export function queryGoodsExportLog(
  current,
  size,
  className
) {
  return request({
    url: '/admin/dataCenter/api/v1/data/export/page',
    method: 'post',
    data: {
      current,
      size,
      className
    }
  });
}

// 商家后台-中台-商品管理-商品列表-导出记录列表-重新导出
export function queryReExport(
  taskId,
  fileName
) {
  return request({
    url: '/admin/dataCenter/api/v1/data/export/reExport',
    method: 'get',
    params: {
      taskId,
      fileName
    }
  });
}

// 产品线
export function getGoodsLine() {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/getGoodsLine',
    method: 'get',
    params: {}
  });
}
// 获取商品审核详情
export function getProductsAuditList(current, goodsId, size) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/auditList',
    method: 'post',
    data: {
      current,
      goodsId,
      size
    }
  });
}
// 批量设置产品线
export function setProductsLines(goodsLineList) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/updateGoodsLine',
    method: 'post',
    data: {
      goodsLineList
    }
  });
}

//商品详情
export function getProductDetail(goodsId) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/detail',
    method: 'get',
    params: {
      goodsId
    }
  });
}
// 新增或编辑商品
export function addOrEditProducts(
  id,
  eid,
  goodsLineInfo,
  goodsType,
  isCn,
  sellSpecificationsId,
  standardId,
  licenseNo,
  manufacturer,
  manufacturerAddress,
  name,
  specifications,
  unit
) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/saveOrUpdate',
    method: 'post',
    data: {
      id,
      eid,
      goodsLineInfo,
      goodsType,
      isCn,
      sellSpecificationsId,
      standardId,
      licenseNo,
      manufacturer,
      manufacturerAddress,
      name,
      specifications,
      unit
    }
  });
}
// 编辑后，重新提交商品接口
export function againProducts(data) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/saveOrUpdate',
    method: 'post',
    data: {
      // 商品库ID
      id: data.id,
      // 商品名称
      name: data.name,
      // 只有以岭登录才显示，才有所属公司ID选择
      eid: data.eid,
      // 注册证号
      licenseNo: data.licenseNo,
      // 生产厂家
      manufacturer: data.manufacturer,
      // 生产厂家地址
      manufacturerAddress: data.manufacturerAddress,
      // 自己公司销售规格
      specifications: data.specifications,
      // 规格单位
      unit: data.unit,
      // 图片信息
      picBasicsInfoList: data.picBasicsInfoList,
      // 商品类型
      goodsType: data.goodsType,
      // 是否国产
      isCn: data.isCn,
      // 商品库ID
      standardId: data.standardId,
      // 中药饮片说明书信息
      decoctionInstructionsInfo: data.decoctionInstructionsInfo,
      // 消杀品说明书信息
      disinfectionInstructionsInfo: data.disinfectionInstructionsInfo,
      // 食品说明书信息
      foodsInstructionsInfo: data.foodsInstructionsInfo,
      // 药品说明书信息
      goodsInstructionsInfo: data.goodsInstructionsInfo,
      // 保健食品说明书信息
      healthInstructionsInfo: data.healthInstructionsInfo,
      // 中药材说明书信息
      materialsInstructionsInfo: data.materialsInstructionsInfo,
      // 医疗器械说明书信息
      medicalInstrumentInfo: data.medicalInstrumentInfo,
      // 配方颗粒说明书信息
      dispensingGranuleInfo: data.dispensingGranuleInfo,
      // 商品库售卖规格ID
      sellSpecificationsId: data.sellSpecificationsId
    }
  });
}
// 新增规格
export function againProductsAddNorms(data) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/saveOrUpdate',
    method: 'post',
    data: {
      // id: data.id,
      specifications: data.specifications,
      unit: data.unit,
      picBasicsInfoList: data.picBasicsInfoList,
      // sellSpecificationsId: data.sellSpecificationsId,
      standardId: data.id
    }
  });
}

// ===================添加商品========================================

// 获取标准库商品列表
export function getStandardProductsList(
  current,
  size,
  name,
  licenseNo,
  manufacturer
) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/standard/goodsList',
    method: 'post',
    data: {
      current,
      size,
      name,
      licenseNo,
      manufacturer
      // controlType: 0,
      // goodsType: 0,
      // isYb: 0,
      // manufacturerAddress: "",
      // name: "",
      // otcType: 0,
      // pictureFlag: 0,
      // specialComposition: 0,
      // standardCategoryId1: 0,
      // standardCategoryId2: 0
    }
  });
}

// 获取标准库商品规格
export function getStandardProductsSpecificationList(id) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/standard/specificationList',
    method: 'get',
    params: { id }
  });
}
// 获取以岭主体企业列表
export function getEnterpriseMainPart() {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/mainPart/list',
    method: 'get'
  });
}

// 根据规格获取商品信息
export function getStandardStandardGoodsDetail(specificationId, standardId) {
  return request({
    url: '/admin/dataCenter/api/v1/data/goods/standard/standardGoods',
    method: 'get',
    params: {
      specificationId,
      standardId
    }
  });
}
