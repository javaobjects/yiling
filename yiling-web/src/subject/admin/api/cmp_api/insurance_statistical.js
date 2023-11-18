import request from '@/subject/admin/utils/request'

// 统计-药盒二维码单页统计
export function QrCodeStatistics(
  current,
  size,
  startTime,
  endTime
) {
  return request({
    url: '/hmc/api/v1/qrcode/statistics/queryPage',
    method: 'get',
    params: {
      current,
      size,
      startTime,
      endTime
    }
  });
}
// 统计-药盒二维码单页统计-合计
export function QrCodeStatisticsTotal(
  startTime,
  endTime
) {
  return request({
    url: '/hmc/api/v1/qrcode/statistics/getTotal',
    method: 'get',
    params: {
      startTime,
      endTime
    }
  });
}
// 商家销售人员下拉选单
export function querySellerUser() {
  return request({
    url: '/hmc/api/v1/drug/welfare/statistics/querySellerUser',
    method: 'post',
    data: {}
  });
}
// 用药福利计划统计列表
export function queryPage(
  // 用户id	
	userId,
  // 福利计划id	
	drugWelfareId,
  // 商家id
	eid,
  // 销售人员id
  sellerUserId,
  // 服药人姓名
	medicineUserName,
  // 服药人手机号
	medicineUserPhone,
  // 用户入组id
	joinGroupId,
  // 入组开始时间
	startTime,
  // 入组结束时间
	endTime,
  // 第几页，默认：1
  current,
  // 每页记录数，默认：10
	size
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/statistics/queryPage',
    method: 'post',
    data: {
      userId,
      drugWelfareId,
      eid,
      sellerUserId,
      medicineUserName,
      medicineUserPhone,
      joinGroupId,
      startTime,
      endTime,
      current,
      size
    }
  });
}
// 用药福利计划统计列表 
export function queryCouponList(
  // 用户入组id（取列表中id字段的值）
  groupId
) {
  return request({
    url: '/hmc/api/v1/drug/welfare/statistics/queryCouponList',
    method: 'get',
    params: {
      groupId
    }
  });
}
