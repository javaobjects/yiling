import request from '@/subject/admin/utils/request';

// 获取商品列表
export function getProductList(current, size, name) {
  return request({
    url: '/pop/api/v1/admin/goods/enterpriseList',
    method: 'post',
    data: {
      current,
      name,
      size
    }
  });
}

// 供应商详情获取商品列表
export function getCompanyProductList(
  current,
  size,
  eid,
  // 产品状态
  goodsStatus,
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
    url: '/pop/api/v1/admin/goods/list',
    method: 'post',
    data: {
      current,
      size,
      eid,
      goodsStatus: goodsStatus === '0' ? null : parseFloat(goodsStatus),
      licenseNo,
      manufacturer,
      name,
      sellSpecifications,
      standardCategoryId1,
      standardCategoryId2,
      standardId
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

// 修改商品状态
export function changeProductInfo(goodsStatus, goodsId, price, qty) {
  return request({
    url: '/pop/api/v1/admin/goods/edit',
    method: 'post',
    data: {
      goodsStatus,
      goodsId,
      price,
      qty
    }
  });
}

// 获取招标挂网价列表
export function getInviteList(current, size, name, licenseNo, manufacturer) {
  return request({
    url: '/pop/api/v1/admin/goods/goodsBiddingPricePageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      licenseNo,
      manufacturer
    }
  });
}

// 根据id查询招标挂网价详情
export function getInviteDetail(goodsId) {
  return request({
    url: '/pop/api/v1/admin/goods/queryGoodsBiddingLocationPrice',
    method: 'get',
    params: {
      goodsId
    }
  });
}

// 修改招标挂网价
export function editInvitePrice(goodsId, price, locationCode) {
  return request({
    url: '/pop/api/v1/admin/goods/editBiddingPrice',
    method: 'post',
    data: {
      goodsId,
      price,
      locationCode
    }
  });
}
