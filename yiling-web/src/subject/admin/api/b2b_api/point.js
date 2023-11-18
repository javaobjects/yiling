import request from '@/subject/admin/utils/request'

// 获取积分列表
export function getPointList(
  current,
  size,
  // 采购商名称
  customerName,
  // 开始时间
  startTime,
  // 结束时间
  endTime
) {
  return request({
    url: '/b2b/api/v1/integral/queryListPage',
    method: 'post',
    data: {
      current,
      customerName,
      startTime,
      size,
      endTime
    }
  })
}

// 获取积分统计值
export function getPointTotal(
  current,
  size,
  // 采购商名称
  customerName,
  // 开始时间
  startTime,
  // 结束时间
  endTime
) {
  return request({
    url: '/b2b/api/v1/integral/countIntegral',
    method: 'post',
    data: {
      current,
      customerName,
      startTime,
      size,
      endTime
    }
  })
}
