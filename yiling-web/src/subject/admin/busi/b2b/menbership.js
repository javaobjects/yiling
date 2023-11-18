import { getDict } from '@/subject/admin/utils';

// 会员退款审核状态
export function memberReturnAuthStatus() {
  return getDict('member_return_auth_status');
}

// 会员退款状态
export function memberReturnStatus() {
  return getDict('member_return_status');
}

// 会员数据来源
export function memberDataSource() {
  return getDict('member_data_source');
}

// 会员开通类型
export function memberOpenType() {
  return getDict('member_open_type');
}