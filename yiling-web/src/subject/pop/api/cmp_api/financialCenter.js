import request from '@/subject/pop/utils/request';
// 药品结算账单主信息查询
export function queryPayAmount() {
  return request({
    url: '/admin/hmc/api/v1/settlement/enterprise/queryPayAmount',
    method: 'post',
    data: {}
  });
}
// 药品结算账单分页
export function pageList(
  current,
  size,
  // 创建开始时间
  startTime,
  // 创建截止时间
  stopTime,
  // 药品终端结算状态 1-待结算/2-已打款/3-无需结算失效单
  terminalSettleStatus
) {
  return request({
    url: '/admin/hmc/api/v1/settlement/enterprise/pageList',
    method: 'post',
    data: {
      current,
      size,
      startTime,
      stopTime,
      terminalSettleStatus
    }
  });
}