import request from '@/subject/admin/utils/request'

// 管理后台供应商查询
export function getProductList(current, size, name) {
  return request({
    url: '/dataCenter/api/v1/admin/goods/enterpriseList',
    method: 'post',
    data: {
      current,
      name,
      size
    }
  })
}

// 供应商详情获取商品列表
export function getCompanyProductList(
  current,
  size,
  eid,
  // 产品状态
  auditStatus,
  // 注册证号
  licenseNo,
  // 生产厂家
  manufacturer,
  name,
  // 销售规格
  sellSpecifications,
  // 标准库一级分类id
  standardCategoryId1,
  // 标准库二级分类id
  standardCategoryId2,
  // 标准库ID
  standardId
) {
  return request({
    url: '/dataCenter/api/v1/admin/goods/list',
    method: 'post',
    data: {
      current,
      size,
      eid,
      auditStatus: auditStatus === '0' ? null : parseFloat(auditStatus),
      licenseNo,
      manufacturer,
      name,
      sellSpecifications,
      standardCategoryId1,
      standardCategoryId2,
      standardId
    }
  })
}

// 查询分类树
export function getProductCategory() {
  return request({
    url: '/dataCenter/api/v1/standard/goods/category/getAll',
    method: 'post',
    data: {
    }
  })
}

// 修改商品状态
export function changeProductInfo(
  goodsStatus,
  goodsIds
) {
  return request({
    url: '/b2b/api/v1/b2b/goods/updateStatus',
    method: 'post',
    data: {
      goodsStatus,
      goodsIds
    }
  })
}
