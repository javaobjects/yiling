import request from '@/subject/pop/utils/request';

// pop 后台首页 获取订单数据
export function getOrderNumber() {
  return request({
    url: '/admin/pop/api/v1/index/get/order/number',
    method: 'get',
    params: {},
    // 错误自己处理
    noErr: true
  });
}
