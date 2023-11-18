// 使用反馈
import request from '@/subject/admin/utils/request'

export function queryPage(
  // 类型
  source,
  // 开始时间
  startTime,
  // 结束时间
  endTime,
  // 第几页，默认：1
  current,
  // 每页记录数，默认：10
  size
){
  return request({
    url: '/cms/api/v1/feedback/queryPage',
    method: 'get',
    params: {
      source,
      startTime,
      endTime,
      current,
      size
    }
  })
}