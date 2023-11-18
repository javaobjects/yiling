import request from '@/subject/admin/utils/request';
// 获取销售团队成员列表，
export function saleTeamManager(
  current,
  mobilePhone,
  name,
  registerEndTime,
  registerStartTime,
  size
) {
  return request({
    url: '/salesAssistant/api/v1/teamManager/queryTeamListPage',
    method: 'post',
    data: {
      current,
      mobilePhone,
      name,
      registerEndTime,
      registerStartTime,
      size
    }
  });
}
// 获取成员所属团队
export function getTeamManager(userId) {
  return request({
    url: '/salesAssistant/api/v1/teamManager/getTeam',
    method: 'get',
    params: {
      userId
    }
  });
}
// 获取成员信息
export function getMemberDetail(userId) {
  return request({
    url: '/salesAssistant/api/v1/teamManager/getMember',
    method: 'get',
    params: {
      userId
    }
  });
}

//  获取团队成员订单列表
export function getMemberOrderList(userId) {
  return request({
    url: '/salesAssistant/api/v1/teamManager/getMemberOrderDetail',
    method: 'get',
    params: {
      userId
    }
  });
}
// 获取团队成员客户列表
export function getTeamCustomerList(userId) {
  return request({
    url: '/salesAssistant/api/v1/teamManager/getMemberCustomerDetail',
    method: 'get',
    params: {
      userId
    }
  });
}

// 获取客户信息弹窗信息
export function getTeamCustomerDetail(current, customerEid, size, userId) {
  return request({
    url: '/salesAssistant/api/v1/teamManager/getCustomerDetail',
    method: 'post',
    data: {
      current,
      customerEid,
      size,
      userId
    }
  });
}
// 获取订单列表弹窗信息
export function getTeamOrderListShow(orderId) {
  return request({
    url: '/salesAssistant/api/v1/teamManager/getOrderDetail',
    method: 'get',
    params: {
      orderId
    }
  });
}
