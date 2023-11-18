import request from '@/subject/pop/utils/request';
// 列表查询福利计划下拉单选
export function queryDrugWelfareList() {
  return request({
    url: '/admin/hmc/api/v1/drug/welfare/statistics/queryDrugWelfareList',
    method: 'post',
    data: {}
  });
}
// 商家销售人员下拉单选
export function querySellerUser() {
  return request({
    url: '/admin/hmc/api/v1/drug/welfare/statistics/querySellerUser',
    method: 'post',
    data: {}
  });
}
// 药品福利计划终端商家列表
export function queryPage(
  // 福利计划id
	drugWelfareId,
  // 销售人员id	
	sellerUserId,
  // 服药人姓名	
	medicineUserName,
  // 服药人手机号	
	medicineUserPhone,
  // 用户入组id	
	groupId,
  // 开始时间	
	startTime,
  // 结束时间
	endTime,
  // 第几页，默认：1
  current,
  // 每页记录数，默认：10
	size
) {
  return request({
    url: '/admin/hmc/api/v1/drug/welfare/statistics/queryPage',
    method: 'post',
    data: {
      drugWelfareId,
      sellerUserId,
      medicineUserName,
      medicineUserPhone,
      groupId,
      startTime,
      endTime,
      current,
      size
    }
  });
}
// 福利券使用统计列表
export function queryCouponList(
  // 用户入组id（取列表中id字段的值）
  groupId
) {
  return request({
    url: '/admin/hmc/api/v1/drug/welfare/statistics/queryCouponList',
    method: 'get',
    params: {
      groupId
    }
  });
}
// 核销记录
export function queryVerificationList(
  // 福利计划id
	drugWelfareId,
  // 核销开始时间
	startTime,
  // 核销结束时间
	endTime,
  current,
	size
) {
  return request({
    url: '/admin/hmc/api/v1/drug/welfare/statistics/queryVerificationList',
    method: 'post',
    data: {
      drugWelfareId,
      startTime,
      endTime,
      current,
      size
    }
  });
}
// 确定核销
export function verificationDrugWelfareGroupCoupon(
  // 福利券码
  couponCode
) {
  return request({
    url: '/admin/hmc/api/v1/drug/welfare/statistics/verificationDrugWelfareGroupCoupon',
    method: 'post',
    data: {
      couponCode
    }
  });
}