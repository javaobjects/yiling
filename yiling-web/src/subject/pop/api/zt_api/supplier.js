import request from '@/subject/pop/utils/request'

//供应商分页列表
export function pageList(
  //供应商ID
  eid,
  //社会统一信用代码
  licenseNumber,
  //ERP供应商名称
  erpName,
  //采购员名称
  buyerName,
  current,
  size
) {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/supplier/pageList',
    method: 'post',
    data: {
      eid,
      licenseNumber,
      erpName,
      buyerName,
      current,
      size
    }
  })
}
//选择供应商下拉列表
export function chooseSupplierList(
  //企业名称
  name,
  current,
  size
) {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/supplier/chooseSupplierList',
    method: 'post',
    data: {
      name,
      current,
      size
    }
  })
}
//编辑保存
export function update(
  id,
  //ERP供应商名称
  erpName,
  //ERP编码
  erpCode,
  //ERP内码
  erpInnerCode,
  //采购员编码
  buyerCode,
  //采购员名称
  buyerName
) {
  return request({
    url: '/admin/dataCenter/api/v1/enterprise/supplier/update',
    method: 'post',
    data: {
      id,
      erpName,
      erpCode,
      erpInnerCode,
      buyerCode,
      buyerName
    }
  })
}