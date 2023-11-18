import request from '@/subject/pop/utils/request';
// 获取商品列表
export function getCmpGoodsList(current, size, goodsStatus, name) {
  return request({
    url: '/admin/hmc/api/v1/hmc/goods/list',
    method: 'get',
    params: {
      current,
      size,
      goodsStatus,
      name
    }
  });
}
// 修改商品库存
export function editSkuNumber(inventoryQty, skuId) {
  return request({
    url: '/admin/hmc/api/v1/hmc/goods/updateGoodsInventoryBySku',
    method: 'post',
    data: {
      inventoryQty,
      skuId
    }
  });
}
//修改价格库存信息
export function updateGoodsPriceAndStock(
  id,
  //销售价
  salePrice,
  //成本价
  marketPrice,
  //库存
  stock,
  //安全库存
  safeStock
) {
  return request({
    url: '/admin/hmc/api/v1/hmc/goods/updateGoodsPriceAndStock',
    method: 'post',
    data: {
      id,
      salePrice,
      marketPrice,
      stock,
      safeStock
    }
  });
}
