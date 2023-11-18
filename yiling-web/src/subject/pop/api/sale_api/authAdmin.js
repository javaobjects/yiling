import request from '@/subject/pop/utils/request';

// 销售助手-权限管理-公司可售区域
export function getSaleArea(
) {
  return request({
    url: '/admin/salesAssistant/api/v1/shop/getSaleArea',
    method: 'get',
    params: {
    }
  });
}

// 销售助手-权限管理-店铺销售区域详情
export function getShop(
) {
  return request({
    url: '/admin/salesAssistant/api/v1/shop/getShop',
    method: 'get',
    params: {
    }
  });
}
