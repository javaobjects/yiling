import request from '@/subject/pop/utils/request';

// 首页获取订单数据
export function getOrderNumber() {
  return request({
    url: '/admin/b2b/api/v1/index/order/number',
    method: 'get',
    params: {}
  });
}
