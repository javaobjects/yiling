import request from '@/subject/admin/utils/request'

// 保险药品管控列表
export function getInsuranceGoodsList(
  current,
  size,
  // 药品名称
  name 
) {
  return request({
    url: '/hmc/api/v1/control/goods/queryPage',
    method: 'get',
    params: {
      current,
      size,
      name
    }
  });
}
// 保险药品管控列表-价格修改
export function insuranceGoodsUpdate(
  id,
  insurancePrice,
  marketPrice
) {
  return request({
    url: '/hmc/api/v1/control/goods/update',
    method: 'post',
    data: {
      id,
      insurancePrice,
      marketPrice
    }
  });
}
// 保险药品管控列表-新增-选择药品弹窗列表
export function queryStandardSpecificationPage(
  current,
  size,
  licenseNo
) {
  return request({
    url: '/hmc/api/v1/control/goods/queryStandardSpecificationPage',
    method: 'get',
    params: {
      current,
      size,
      licenseNo
    }
  });
}
// 药品商家提成设置-提成设置-选择药品弹窗列表
export function queryPageInsuranceGoods(
  current,
  size,
  name
) {
  return request({
    url: '/hmc/api/v1/control/goods/pageInsuranceGoods',
    method: 'get',
    params: {
      current,
      size,
      name
    }
  });
}
// 保险服务商服务内容-新建服务内容-选择药品弹窗列表
export function queryInsuranceContentGoods(
  current,
  size,
  name,
  insuranceCompanyId
) {
  return request({
    url: '/hmc/api/v1/control/goods/pageInsuranceCompanyGoods',
    method: 'get',
    params: {
      current,
      size,
      name,
      insuranceCompanyId
    }
  });
}

// 保险药品管控列表-新增
export function addGoods(
  insurancePrice,
  marketPrice,
  sellSpecificationsId,
  standardId
) {
  return request({
    url: '/hmc/api/v1/control/goods/add',
    method: 'post',
    data: {
      insurancePrice,
      marketPrice,
      sellSpecificationsId,
      standardId
    }
  });
}
// 保险药品管控-渠道管控分页列表
export function queryPurchaseList(
  current,
  size,
  goodsControlId,
  enterpriseName
) {
  return request({
    url: '/hmc/api/v1/goods/purchase/queryPage',
    method: 'get',
    params: {
      current,
      size,
      goodsControlId,
      enterpriseName
    }
  });
}
// 保险药品管控-销售管控-保险药品渠道管控-渠道商搜索列表
export function queryEnterpriseList(
  name
) {
  return request({
    url: '/hmc/api/v1/goods/purchase/queryEnterprise',
    method: 'get',
    params: {
      name
    }
  });
}
// 保险药品管控-销售管控-保险药品渠道管控-添加管控渠道
export function addPurchase(
  goodsControlId,
  sellerEid,
  channelType
) {
  return request({
    url: '/hmc/api/v1/goods/purchase/add',
    method: 'post',
    data: {
      goodsControlId,
      sellerEid,
      channelType
    }
  });
}
// 保险药品管控-销售管控-保险药品渠道管控-编辑管控渠道
export function updatePurchase(
  id,
  controlStatus,
  channelType
) {
  return request({
    url: '/hmc/api/v1/goods/purchase/update',
    method: 'post',
    data: {
      id,
      controlStatus,
      channelType
    }
  });
}
// 保险药品管控-销售管控-保险药品渠道管控-详情接口
export function purchaseGetDetail(
  id
) {
  return request({
    url: '/hmc/api/v1/goods/purchase/getDetail',
    method: 'get',
    params: {
      id
    }
  });
}

// 保险服务商列表
export function getInsuranceCompanyList(
  current,
  size,
  companyName // 保险服务商名称
) {
  return request({
    url: '/hmc/api/v1/insurance/company/pageList',
    method: 'post',
    data: {
      current,
      size,
      companyName
    }
  });
}
// 保险服务商详情
export function getInsuranceCompanyDetail(
  id
) {
  return request({
    url: '/hmc/api/v1/insurance/company/queryById',
    method: 'get',
    params: {
      id
    }
  });
}
// 保险服务商启用停用
export function modifyStatus(
  id,
  status
) {
  return request({
    url: '/hmc/api/v1/insurance/company/modifyStatus',
    method: 'post',
    data: {
      id,
      status
    }
  });
}
// 删除保险服务商
export function deleteInsuranceCompany(
  id
) {
  return request({
    url: '/hmc/api/v1/insurance/company/delete',
    method: 'post',
    data: {
      id
    }
  });
}
// 新建/编辑保险服务商
export function addInsuranceCompany(
  companyName, // 保险服务商名称
  insuranceNo, // 执业许可证号/社会信用统一代码
  provinceCode, // 省
  cityCode, // 市
  regionCode, // 区
  address, // 详细地址
  cancelInsuranceTelephone, // 退保客服电话
  cancelInsuranceAddress, // 退保地址
  renewInsuranceAddress, // 续保地址
  internetConsultationUrl, // 互联网问诊地址
  remark, // 备注
  contactName, // 联系人姓名
  status, // 保险服务商状态 1-启用 2-停用 (新建默认给开启)
  // 代理理赔协议地址
  claimProtocolUrl,
  // ID 编辑状态用
  id
) {
  return request({
    url: '/hmc/api/v1/insurance/company/save',
    method: 'post',
    data: {
      companyName,
      insuranceNo,
      provinceCode,
      cityCode,
      regionCode,
      address,
      cancelInsuranceTelephone,
      cancelInsuranceAddress,
      renewInsuranceAddress,
      internetConsultationUrl,
      remark,
      contactName,
      status,
      claimProtocolUrl,
      id
    }
  });
}
// 保险服务商-服务内容分页列表
export function queryInsuranceContentList(
  insuranceCompanyId,
  current,
  size,
  insuranceName, // 保险名称
  startTime,
  stopTime
) {
  return request({
    url: '/hmc/api/v1/insurance/insurance/pageList',
    method: 'post',
    data: {
      insuranceCompanyId,
      current,
      size,
      insuranceName,
      startTime,
      stopTime
    }
  });
}
// 保险服务商-服务内容-新建/编辑
export function insuranceContentUpdate(
  insuranceCompanyId, // 保险公司id
  insuranceName, // 保险名称
  payAmount, // 售卖金额
  quarterIdentification, // 定额类型季度标识
  yearIdentification, // 定额类型年标识
  serviceRatio, // 服务商扣服务费比例
  url, // 售卖地址--h5页面链接
  insuranceDetailSaveList, // 保险商品明细新增信息,
  status, // 服务内容 1-启用 2-停用
  id
) {
  return request({
    url: '/hmc/api/v1/insurance/insurance/save',
    method: 'post',
    data: {
      insuranceCompanyId,
      insuranceName,
      payAmount,
      quarterIdentification,
      yearIdentification,
      serviceRatio,
      url,
      insuranceDetailSaveList,
      status,
      id
    }
  });
}
// 保险服务商-服务内容-状态启用停用
export function insuranceContentStatusUpdate(
  id,
  status // 服务内容 1-启用 2-停用
) {
  return request({
    url: '/hmc/api/v1/insurance/insurance/modifyStatus',
    method: 'post',
    data: {
      id,
      status
    }
  });
}

// 药品商家提成设置-列表
export function enterpriseAccountList(
  current,
  size,
  ename
) {
  return request({
    url: '/hmc/api/v1/enterprise/account/pageList',
    method: 'post',
    data: {
      current,
      size,
      ename
    }
  });
}
// 药品商家提成设置-列表项-详情
export function enterpriseDetail(
  id
) {
  return request({
    url: '/hmc/api/v1/enterprise/account/queryDetailById',
    method: 'get',
    params: {
      id
    }
  });
}
// 药品商家提成设置-列表项-结账账户设置-详情
export function enterpriseAccountDetail(
  id
) {
  return request({
    url: '/hmc/api/v1/enterprise/account/queryById',
    method: 'get',
    params: {
      id
    }
  });
}
// 药品商家提成设置-列表项-结账账户设置-新增修改
export function enterpriseAccountSave(
  eid,
  ename,
  accountBank,
  accountName,
  accountNumber,
  accountType,
  remark,
  id
) {
  return request({
    url: '/hmc/api/v1/enterprise/account/save',
    method: 'post',
    data: {
      id,
      eid,
      ename,
      accountBank,
      accountName,
      accountNumber,
      accountType,
      remark
    }
  });
}
// 药品商家提成设置-列表项-提成设置-详情
export function enterpriseGoodsDetail(
  eid
) {
  return request({
    url: '/hmc/api/v1/goods/goods/queryByEId',
    method: 'get',
    params: {
      eid
    }
  });
}
// 药品商家提成设置-列表项-提成设置-新增/修改
export function enterpriseGoodsUpdate(
  eid,
  ename,
  goodsRequest
) {
  return request({
    url: '/hmc/api/v1/goods/goods/save',
    method: 'post',
    data: {
      eid,
      ename,
      goodsRequest
    }
  });
}
// 药品商家提成设置-新增-选择商家-弹窗列表
export function addEnterpriseAccountList(
  current,
  size,
  ename
) {
  return request({
    url: '/hmc/api/v1/enterprise/account/pageEnterprise',
    method: 'post',
    data: {
      current,
      size,
      ename
    }
  });
}

