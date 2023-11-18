import request from '@/subject/admin/utils/request';
// 获取佣金列表，
export function commissionList(current, size, username, mobile) {
  return request({
    url: '/salesAssistant/api/v1/commissions/queryCommissionsPageList',
    method: 'post',
    data: {
      current,
      size,
      username,
      mobile
    }
  });
}
// 获取用户任务列表
export function commissionUserNameTaskList(current, size, userId) {
  return request({
    url: '/salesAssistant/api/v1/commissions/queryTaskPageList',
    method: 'post',
    data: {
      current,
      size,
      userId
    }
  });
}
// 获取用户任务 查看明细
export function commissionUserNameTaskListDetail(commissionsId,current, size ) {
  return request({
    url: '/salesAssistant/api/v1/commissions/queryCommissionsDetailPageList',
    method: 'post',
    data: {
      commissionsId,
      current,
      size
    }
  });
}

