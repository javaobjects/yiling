import request from '@/subject/pop/utils/request'

// 分页查询厂家
export function queryManufacturerListPage(
  current,
  size,
  eid,//厂家编号
  ename//厂家名称
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/queryManufacturerListPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename
    }
  })
}

// 新增厂家
export function addManufacturer(
  eid,//厂家编号
  ename,//厂家名称
  type//厂家类型 1-生产厂家 2-品牌厂家
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/addManufacturer',
    method: 'post',
    data: {
      eid,
      ename,
      type
    }
  })
}

// 删除厂家
export function deleteManufacturer(
  id
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/deleteManufacturer',
    method: 'get',
    params: {
      id
    }
  })
}

// 根据企业ID查询中台企业
export function getEnterpriseById(
  id
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/getEnterpriseById',
    method: 'get',
    params: {
      id
    }
  })
}

// 厂家商品分页列表
export function queryManufacturerGoodsListPage(
  current,
  size,
  manufacturerId,//厂家ID
  goodsId//商品ID
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/queryManufacturerGoodsListPage',
    method: 'post',
    data: {
      current,
      size,
      manufacturerId,
      goodsId
    }
  })
}

// 选择商品分页列表
export function queryGoodsPageList(
  current,
  size,
  goodsId,//厂家ID
  goodsName,//商品ID
  eid,// 甲方ID（选择供销条款的商品时，此字段必填）
  firstType// 甲方类型：1-工业生产厂家 2-工业品牌厂家 3-商业供应商 4-代理商（选择供销条款的商品时，此字段必填）
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/queryGoodsPageList',
    method: 'post',
    data: {
      current,
      size,
      goodsId,
      goodsName,
      eid,
      firstType
    }
  })
}

// 添加厂家商品
export function addManufacturerGoods(
  manufacturerId,//厂家ID
  list
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/addManufacturerGoods',
    method: 'post',
    data: {
      manufacturerId,
      list
    }
  })
}

// 删除厂家商品
export function deleteManufacturerGoods(
  idList//厂家商品ID集合
) {
  return request({
    url: '/admin/pop/api/v1/agreementManufacturer/deleteManufacturerGoods',
    method: 'post',
    data: {
      idList
    }
  })
}

// --------------- 协议新增----------------
// 获取子公司列表
export function getSubEnterpriseList(
  secondEid,
  name
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/getSubEnterpriseList',
    method: 'get',
    params: {
      secondEid,
      name
    }
  })
}

// 查询甲方/乙方
export function queryFirstParty(
  current,
  size,
  firstType,
  ename
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/queryFirstParty',
    method: 'post',
    data: {
      current,
      size,
      firstType,
      ename
    }
  })
}

// 查询协议是否存在，存在则返回协议编码
export function checkAgreementExist(
  firstType,
  agreementType,
  eid,
  secondEid,
  startTime,
  endTime
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/checkAgreementExist',
    method: 'post',
    data: {
      firstType,
      agreementType,
      eid,
      secondEid,
      startTime,
      endTime
    }
  })
}

// 查询协议签订人
export function queryStaffPage(
  current,
  size,
  name,
  mobile
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/queryStaffPage',
    method: 'post',
    data: {
      current,
      size,
      name,
      mobile
    }
  })
}

// 查询乙方协议签订人
export function querySecondUserPage(
  current,
  size,
  name,
  mobile,
  secondName
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/querySecondUserPage',
    method: 'post',
    data: {
      current,
      size,
      name,
      mobile,
      secondName
    }
  })
}

// 新建/修改乙方协议签订人
export function saveAgreementSecondUser(
  id,
  name,
  mobile,
  manufacturerType,
  secondEid,
  secondName,
  email
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/saveAgreementSecondUser',
    method: 'post',
    data: {
      id,
      name,
      mobile,
      manufacturerType,
      secondEid,
      secondName,
      email
    }
  })
}

// 删除乙方协议签订人
export function deleteAgreementSecondUser(
  id
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/deleteAgreementSecondUser',
    method: 'get',
    params: {
      id
    }
  })
}

// 查询指定商业公司
export function queryBusinessPage(
  current,
  size,
  ename
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/queryBusinessPage',
    method: 'post',
    data: {
      current,
      size,
      ename
    }
  })
}

// 新建协议
export function createAgreement(
  // 协议主条款
  agreementMainTerms,
  // 协议供销条款
  agreementSupplySalesTerms,
  // 协议结算条款
  agreementSettlementTerms,
  // 协议返利条款
  agreementRebateTerms
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/createAgreement',
    method: 'post',
    data: {
      agreementMainTerms,
      agreementSupplySalesTerms,
      agreementSettlementTerms,
      agreementRebateTerms
    }
  })
}

// 查询协议审核列表
export function queryAgreementAuthPage(
  current,
  size,
  startCreateTime,
  endCreateTime,
  agreementNo,
  mainUserName,
  billsType,
  authStatus,
  agreementType,
  ename,
  secondName
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/queryAgreementAuthPage',
    method: 'post',
    data: {
      current,
      size,
      startCreateTime,
      endCreateTime,
      agreementNo,
      mainUserName,
      billsType,
      authStatus,
      agreementType,
      ename,
      secondName
    }
  })
}

// 查询协议分页列表
export function queryAgreementPage(
  current,
  size,
  startTime,
  endTime,
  agreementNo,
  mainUserName,
  startCreateTime,
  endCreateTime,
  agreementType,
  buyChannel,
  secondName,
  goodsId,
  ename,
  rebateStandard,
  activeFlag,
  startAuthPassTime,
  endAuthPassTime,
  businessOperatorName,
  effectStatus
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/queryAgreementPage',
    method: 'post',
    data: {
      current,
      size,
      startTime,
      endTime,
      agreementNo,
      mainUserName,
      startCreateTime,
      endCreateTime,
      agreementType,
      buyChannel,
      secondName,
      goodsId,
      ename,
      rebateStandard,
      activeFlag,
      startAuthPassTime,
      endAuthPassTime,
      businessOperatorName,
      effectStatus
    }
  })
}

// 删除乙方协议签订人
export function getDetail(
  id
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/getDetail',
    method: 'get',
    params: {
      id
    }
  })
}

// 协议归档
export function updateArchiveAgreement(
  id,
  archiveNo,
  archiveRemark
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/updateArchiveAgreement',
    method: 'post',
    data: {
      id,
      archiveNo,
      archiveRemark
    }
  })
}

// 协议审核
export function updateAuthAgreement(
  id,
  paperNo,
  authStatus,
  authRejectReason
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/updateAuthAgreement',
    method: 'post',
    data: {
      id,
      paperNo,
      authStatus,
      authRejectReason
    }
  })
}

// 查询协议商品列表
export function queryAgreementGoodsPage(
  current,
  size,
  id
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/queryAgreementGoodsPage',
    method: 'post',
    data: {
      current,
      size,
      id
    }
  })
}

// 协议导入选择协议列表
export function queryImportAgreementList(
  ename,
  secondName
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/queryImportAgreementList',
    method: 'post',
    data: {
      ename,
      secondName
    }
  })
}

// 获取企业商品总数
export function getGoodsNumberByEid(
  eid,
  firstType
) {
  return request({
    url: '/admin/pop/api/v1/agreementTerms/getGoodsNumberByEid',
    method: 'get',
    params: {
      eid,
      firstType
    }
  })
}