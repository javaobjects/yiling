import request from '@/subject/admin/utils/request'

// 统计报表-数据信息-数据统计
export function getStatistics() {
  return request({
    url: '/dataCenter/api/v1/report/order/statistics',
    method: 'get',
    params: {}
  })
}