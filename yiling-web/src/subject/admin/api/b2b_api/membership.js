import request from '@/subject/admin/utils/request'

// 获取会员列表
export function getMemberListPage(
  current,
  size,
  // 买家名称
  memberName,
  startCreateTime,
  endCreateTime,
  //会员ID
  memberId
) {
  return request({
    url: '/b2b/api/v1/member/queryMemberListPage',
    method: 'post',
    data: {
      current,
      size,
      memberName,
      startCreateTime,
      endCreateTime,
      memberId
    }
  })
}

// 开始/停止获得
export function stopGetMember(id) {
  return request({
    url: '/b2b/api/v1/member/stopGetMember',
    method: 'get',
    params: {
      id
    }
  })
}

// 创建会员
export function createMember(
  // 会员名称
  name,
  // 会员描述
	description,
  // 会员点亮图	
	lightPicture,
  // 会员熄灭图	
	extinguishPicture,
  // 背景图	
  bgPicture,
  // 获得条件集合
  memberBuyStageList,
	// 会员权益集合
	memberEquityList,
	// 是否续卡提醒：0-否 1-是
	renewalWarn,
  // 到期前提醒天数
  warnDays,
  // 排序
	sort
) {
  return request({
    url: '/b2b/api/v1/member/createMember',
    method: 'post',
    data: {
      name,
      description,
      lightPicture,
      extinguishPicture,
      bgPicture,
      memberBuyStageList,
      memberEquityList,
      renewalWarn,
      warnDays,
      sort
    }
  })
}

// 更新会员
export function updateMember(
  // 会员id
  id,
  // 会员名称
  name,
  // 会员描述
  description,
  // 会员点亮图
  lightPicture,
  // 会员熄灭图
  extinguishPicture,
  // 背景图
  bgPicture,
  // 获得条件集合
  memberBuyStageList,
  // 会员权益集合
  memberEquityList,
  // 是否续卡提醒：0-否 1-是
  renewalWarn,
  // 到期前提醒天数
  warnDays,
  // 排序
  sort
) {
  return request({
    url: '/b2b/api/v1/member/updateMember',
    method: 'post',
    data: {
      id,
      name,
      description,
      lightPicture,
      extinguishPicture,
      bgPicture,
      memberBuyStageList,
      memberEquityList,
      renewalWarn,
      warnDays,
      sort
    }
  })
}

// 获取会员详情
export function getMember(id) {
  return request({
    url: '/b2b/api/v1/member/getMember',
    method: 'get',
    params: {
      id
    }
  })
}

// 权益列表
export function memberEquityQueryList() {
  return request({
    url: '/b2b/api/v1/memberEquity/queryList',
    method: 'post',
    data: {
    }
  })
}

// 权益分页列表
export function memberEquityListPage(
  current,
  size,
  type,
  // 权益名称
  name,
  // 权益状态：0-关闭 1-开启
  status
) {
  return request({
    url: '/b2b/api/v1/memberEquity/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      type,
      name,
      status
    }
  })
}

// 修改权益状态
export function updateStatus(id) {
  return request({
    url: '/b2b/api/v1/memberEquity/updateStatus',
    method: 'get',
    params: {
      id
    }
  })
}

// 删除权益
export function deleteEquity(
  id
) {
  return request({
    url: '/b2b/api/v1/memberEquity/deleteEquity',
    method: 'get',
    params: {
      id
    }
  })
}

// 新增权益
export function createEquity(
  name,
  description,
  icon,
  status
) {
  return request({
    url: '/b2b/api/v1/memberEquity/createEquity',
    method: 'post',
    data: {
      name,
      description,
      icon,
      status
    }
  })
}

// 修改权益
export function updateEquity(
  id,
  name,
  description,
  icon,
  status
) {
  return request({
    url: '/b2b/api/v1/memberEquity/updateEquity',
    method: 'post',
    data: {
      id,
      name,
      description,
      icon,
      status
    }
  })
}

// 获取权益详情
export function getEquity(
  id
) {
  return request({
    url: '/b2b/api/v1/memberEquity/getEquity',
    method: 'get',
    params: {
      id
    }
  })
}

// 获取会员购买记录列表
export function queryBuyRecordListPage(
  current,
  size,
  // 会员名称
  memberName,
  // 终端名称
  ename,
  // 购买开始时间
  buyStartTime,
  // 购买结束时间
  buyEndTime,
  // 推广方名称
  promoterName,
  // 推广方ID
  promoterId,
  // 推广方人名称
  promoterUserName,
  // 推广人ID
  promoterUserId,
  // 终端ID
  eid,
  // 所属城市编码
  cityCode,
  // 是否过期
  expireFlag,
  // 开通类型
  openType,
  // 所属省份编码
  provinceCode,
  // 所属区域编码
  regionCode,
  // 是否退款
  returnFlag,
  // 数据来源
  sourceList,
  //会员ID集合
  memberIdList,
  //推广方省份编码
  promoterProvinceCode,
  //推广方城市编码
  promoterCityCode,
  //推广方区域编码
  promoterRegionCode
) {
  return request({
    url: '/b2b/api/v1/member/queryBuyRecordListPage',
    method: 'post',
    data: {
      current,
      size,
      memberName,
      ename,
      buyStartTime,
      buyEndTime,
      promoterName,
      promoterId,
      promoterUserId,
      promoterUserName,
      eid,
      cityCode,
      expireFlag,
      openType,
      provinceCode,
      regionCode,
      returnFlag,
      sourceList,
      memberIdList,
      promoterProvinceCode,
      promoterCityCode,
      promoterRegionCode
    }
  })
}

// 更新推广人或推广方
export function updateBuyMemberPromoter(addParams) {
  return request({
    url: '/b2b/api/v1/member/updateBuyMemberPromoter',
    method: 'post',
    data: {
      ...addParams
    }
  })
}

// 获取会员退款列表
export function getMemberReturnListPage(
  current,
  size,
  // 终端ID
  eid,
  // 终端名称
  ename,
  // 订单编号
  orderNo,
  applyStartTime,
  applyEndTime,
  // 审核状态：1-待审核 2-已审核 3-已驳回
  authStatus
) {
  return request({
    url: '/b2b/api/v1/memberReturn/queryMemberReturnPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename,
      orderNo,
      applyStartTime,
      applyEndTime,
      authStatus
    }
  })
}

// 退款同意或驳回
export function memberReturn(
  id,
  // 审核状态： 2-已审核 3-已驳回
  authStatus
) {
  return request({
    url: '/b2b/api/v1/memberReturn/authReturn',
    method: 'post',
    data: {
      id,
      authStatus
    }
  })
}

// 获取退款详情
export function getMemberReturnDetail(
  id
) {
  return request({
    url: '/b2b/api/v1/member/getReturnDetail',
    method: 'get',
    params: {
      id
    }
  })
}

// 提交退款申请
export function submitMemberReturn(
  id,
  // 退款原因
  returnReason,
  // 退款备注
  returnRemark,
  // 退款金额
  submitReturnAmount
) {
  return request({
    url: '/b2b/api/v1/member/submitReturn',
    method: 'post',
    data: {
      id,
      returnReason,
      returnRemark,
      submitReturnAmount
    }
  })
}
// 会员列表 排序
export function updateSort(
  //会员ID
  id,
  //排序
	sort
) {
  return request({
    url: '/b2b/api/v1/member/updateSort',
    method: 'post',
    data: {
      id,
      sort
    }
  })
}
// 会员查询
// 企业会员列表
export function queryEnterpriseMemberPage(
  // 企业名称
	ename,
  // 企业ID
  eid,
  // 社会统一信用代码
  licenseNumber,
  // 联系人电话
	contactorPhone,
  // 联系人名称	
	contactor,
  // 企业类型	
	type,
  // 会员状态：1-正常 2-过期
	status,
  // 所属省份编码	
	provinceCode,
  // 所属城市编码
  cityCode,
  // 所属区域编码	
	regionCode,
  // 会员ID集合
	memberIdList,
  // 第几页，默认：1
	current,
  // 每页记录数，默认：10
	size
) {
  return request({
    url: '/b2b/api/v1/enterpriseMember/queryEnterpriseMemberPage',
    method: 'post',
    data: {
      ename,
      eid,
      licenseNumber,
      contactorPhone,
      contactor,
      type,
      status,
      provinceCode,
      cityCode,
      regionCode,
      memberIdList,
      current,
      size
    }
  })
}
//获取所有会员
export function getMemberList() {
  return request({
    url: '/b2b/api/v1/member/getMemberList',
    method: 'get',
    params: {}
  })
}
// 获取企业会员详情
export function getDetail(
  // 会员id
  id
) {
  return request({
    url: '/b2b/api/v1/enterpriseMember/getDetail',
    method: 'get',
    params: {
      id
    }
  })
}
//取消导入购买记录
export function cancelBuyRecord(
  id
) {
  return request({
    url: '/b2b/api/v1/member/cancelBuyRecord',
    method: 'post',
    data: {
      id
    }
  })
}

//取消会员查看
export function cancelBuyRecordListPage(
  current,
  size,
  ename,
  cancelStartTime,
  cancelEndTime,
  promoterId,
  updateUserName,
  promoterUserName,
  promoterUserId,
  eid
) {
  return request({
    url: '/b2b/api/v1/member/queryCancelMemberPageList',
    method: 'post',
    data: {
      current,
      size,
      ename,
      cancelStartTime,
      cancelEndTime,
      promoterId,
      updateUserName,
      promoterUserName,
      promoterUserId,
      eid
    }
  })
}