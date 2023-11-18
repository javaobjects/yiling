import request from '@/subject/pop/utils/request'

// 协议首页统计
export function getAgreementStatistics(
  current,
  size,
  channelId,//渠道类型
  name,//渠道商名称
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber//统一信用代码
) {
  return request({
    url: '/admin/pop/api/v1/agreement/statistics',
    method: 'post',
    data: {
      current,
      size,
      channelId,//渠道类型
      name,//渠道商名称
      provinceCode,//省
      cityCode,//市
      regionCode,//区
      licenseNumber//统一信用代码
    }
  })
}

// 协议首页统计
export function getAgreementList(
  current,
  size,
  channelId,//渠道类型
  name,//渠道商名称
  provinceCode,//省
  cityCode,//市
  regionCode,//区
  licenseNumber//统一信用代码
) {
  return request({
    url: '/admin/pop/api/v1/agreement/list',
    method: 'post',
    data: {
      current,
      size,
      channelId,//渠道类型
      name,//渠道商名称
      provinceCode,//省
      cityCode,//市
      regionCode,//区
      licenseNumber//统一信用代码
    }
  })
}

//根据当前列表的企业id，查询三方协议企业列表
// 获取单个渠道商信息
export function getThirdAgreementEntList(eid, current, size) {
  return request({
    url: '/admin/pop/api/v1/agreement/getThirdAgreementEntList',
    method: 'post',
    data: {
      eid,
      current,
      size
    }
  })
}

// 根据协议状态，，分页查询年度协议列表
export function getAgreementPageList(
  current,
  size,
  agreementStatus,//协议状态：1-进行中 2-未开始 3-已停用 4-已过期
  queryEid
) {
  return request({
    url: '/admin/pop/api/v1/agreement/getAgreementPageList',
    method: 'post',
    data: {
      current,
      size,
      agreementStatus,
      queryEid
    }
  })
}

// 获取客户企业信息
export function getCustomerEnterpriseInfo(customerEid) {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/getCustomerEnterpriseInfo',
    method: 'get',
    params: {
      customerEid
    }
  })
}

// 根据企业id查询主体协议各状态数量
export function getYearAgreementStatusCount(eid) {
  return request({
    url: '/admin/pop/api/v1/agreement/getYearAgreementStatusCount',
    method: 'get',
    params: {
      eid
    }
  })
}

// 根据年度协议id查询议状态数量
export function getTempAgreementStatusCount(parentId) {
  return request({
    url: '/admin/pop/api/v1/agreement/getTempAgreementStatusCount',
    method: 'get',
    params: {
      parentId
    }
  })
}

// 查询三方协议列表状态数量
export function getEntSupplementAgreementCount(queryEid, eid) {
  return request({
    url: '/admin/pop/api/v1/agreement/getEntSupplementAgreementCount',
    method: 'post',
    data: {
      queryEid,
      eid
    }
  })
}

// 根据企业id查询主体协议各状态数量
export function getAgreementStatusCount(
  eid,
  agreementCategory
) {
  return request({
    url: '/admin/pop/api/v1/agreement/getAgreementStatusCount',
    method: 'post',
    data: {
      eid,
      agreementCategory
    }
  })
}

// 根据协议状态，分页查询 补充三方协议 列表
export function getEntSupplementAgreementPageList(
  current,
  size,
  agreementStatus,//协议状态：1-进行中 2-未开始 3-已停用 4-已过期
  queryEid,
  eid
) {
  return request({
    url: '/admin/pop/api/v1/agreement/getEntSupplementAgreementPageList',
    method: 'post',
    data: {
      current,
      size,
      agreementStatus,
      queryEid,
      eid
    }
  })
}

// 根据协议状态及主协议id，分页查询补充协议列表
export function getSupplementAgreementPageList(
  current,
  size,
  agreementStatus,//协议状态：1-进行中 2-未开始 3-已停用 4-已过期
  parentAgreementId//年度协议Id
) {
  return request({
    url: '/admin/pop/api/v1/agreement/getSupplementAgreementPageList',
    method: 'post',
    data: {
      current,
      size,
      agreementStatus,
      parentAgreementId
    }
  })
}

// 根据年度协议id查询年度协议详情
export function getYearAgreementsDetail(currentEid, agreementId) {
  return request({
    url: '/admin/pop/api/v1/agreement/getYearAgreementsDetail',
    method: 'get',
    params: {
      currentEid,
      agreementId
    }
  })
}

// pop后台协议详情获取商品信息
export function getGoodsInfoList(
  current,
  size,
  agreementId // 协议主键id
) {
  return request({
    url: '/admin/pop/api/v1/agreement/goods/get/info',
    method: 'post',
    data: {
      current,
      size,
      agreementId
    }
  })
}

// pop后台商品弹框查询 创建主体协议时，获取所有商品列表
export function getGoodsPopList(
  current,
  size,
  eid,// 	商业主体
  agreementId, // 协议ID
  goodsName,
  licenseNo,
  isPatent
) {
  return request({
    url: '/admin/pop/api/v1/goods/popList',
    method: 'post',
    data: {
      current,
      size,
      eid,
      agreementId,
      goodsName,
      licenseNo,
      isPatent
    }
  })
}

// pop后台协议详情 创建补充协议时,获取商品信息
export function getGoodsList(
  current,
  size,
  agreementId, // 年度协议ID
  tempAgreementId,// 临时协议ID
  goodsName,
  licenseNo,
  isPatent//专利类型 0-全部 1-非专利 2-专利
) {
  return request({
    url: '/admin/pop/api/v1/agreement/goods/list',
    method: 'post',
    data: {
      current,
      size,
      agreementId,
      tempAgreementId,
      goodsName,
      licenseNo,
      isPatent
    }
  })
}

// 获取以岭主体企业列表
export function getAgreementMainPartList() {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/mainPart/list',
    method: 'get',
    params: {}
  })
}

// 根据补充协议id查询协议快照
export function getAgreementAndSnapshotDetail(agreementId) {
  return request({
    url: '/admin/pop/api/v1/agreement/getAgreementAndSnapshotDetail',
    method: 'get',
    params: {
      agreementId
    }
  })
}

// pop后台主协议新增 agreement/saveYear
export function saveYearAgreement(
  eid, // 协议主体ID（甲方
  ename, // 协议主体名字（甲方）
  name, // 协议名称
  content,// 协议描述
  startTime,
  endTime,
  secondChannelName, // 协协客户渠道名称（乙方）
  secondEid, // 协议客户ID（乙方
  secondName, // 协协客户（乙方）
  agreementGoodsList// 协议商品列表
) {
  return request({
    url: '/admin/pop/api/v1/agreement/saveYear',
    method: 'post',
    data: {
      eid, // 协议主体ID（甲方
      ename, // 协议主体名字（甲方）
      name, // 协议名称
      content,// 协议描述
      startTime,
      endTime,
      secondChannelName, // 协协客户渠道名称（乙方）
      secondEid, // 协议客户ID（乙方
      secondName, // 协协客户（乙方）
      agreementGoodsList// 协议商品列表
    }
  })
}

// pop后台补充协议新增
export function saveTempAgreement(addParams) {
  return request({
    url: '/admin/pop/api/v1/agreement/saveTemp',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// pop后台第三方渠道商弹框
export function getThirdEnterpriseList(
  current,
  size,
  buyerEid,
  sellerEid,
  sellerName
) {
  return request({
    url: '/admin/pop/api/v1/agreement/thirdEnterpriseList',
    method: 'post',
    data: {
      current,
      size,
      buyerEid,
      sellerEid,
      sellerName
    }
  })
}

// 根据补充协议id查询协议详情
export function getSupplementAgreementsDetail(agreementId) {
  return request({
    url: '/admin/pop/api/v1/agreement/getSupplementAgreementsDetail',
    method: 'get',
    params: {
      agreementId
    }
  })
}

// 根据补充协议id和快照id查询历史协议详情
export function getSupplementAgreementsDetailBySnapshotId(agreementId, agreementSnapshotId) {
  return request({
    url: '/admin/pop/api/v1/agreement/getSupplementAgreementsDetailBySnapshotId',
    method: 'get',
    params: {
      agreementId,
      agreementSnapshotId
    }
  })
}

// 根据协议id进行停用
export function agreementClose(
  agreementId, // 协议id
  opType // 操作类型 1-删除 2-停用
) {
  return request({
    url: '/admin/pop/api/v1/agreement/agreementClose',
    method: 'post',
    data: {
      agreementId,
      opType
    }
  })
}

// pop后台协议添加商品
export function agreementAddGoods(
  addParams
) {
  return request({
    url: '/admin/pop/api/v1/agreement/edit',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 更新协议接口
export function updateAgreement(
  addParams
) {
  return request({
    url: '/admin/pop/api/v1/agreement/updateAgreement',
    method: 'post',
    data: {
      ...addParams
    }
  })
}
