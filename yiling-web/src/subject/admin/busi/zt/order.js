import { getDict } from '@/subject/admin/utils';

// 运营后台-中台-订单管理-订单列表-活动方式
export function activityType() {
  return getDict('activity_type');
}

// 运营后台-中台-订单管理-订单列表-订单类型
export function orderType() {
  return getDict('order_type');
}
