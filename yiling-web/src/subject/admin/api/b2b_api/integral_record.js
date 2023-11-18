import request from '@/subject/admin/utils/request'
//积分兑换商品分页列表
export function queryListPage(
  //商品名称
  goodsName,
  //上架状态：1-已上架 2-已下架
  shelfStatus,
  //商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
  goodsType,
  //创建人名称
  createUserName,
  //创建人手机号
  mobile,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralExchangeGoods/queryListPage',
    method: 'post',
    data: {
      goodsName,
      shelfStatus,
      goodsType,
      createUserName,
      mobile,
      current,
      size
    }
  })
}
//赠品库列表
export function giftList(
  //所属业务（1-2b)
  businessType,
  //商品编号
  id,
  //品名称
  name
) {
  return request({
    url: '/b2b/api/v1/goods/gift/get/list',
    method: 'post',
    data: {
      businessType,
      id,
      name
    }
  })
}
// 保存积分兑换商品
export function integralSave(
  //ID（修改时必须传入）
  id,
  //物品ID（物品ID或优惠券ID）
  goodsId,
  //物品名称（物品名称或优惠券名称）
  goodsName,
  //商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
  goodsType,
  //兑换所需积分	
  exchangeUseIntegral,
  //可兑换数量	
  canExchangeNum,
  //单品兑换限制（份/用户）	
  singleMaxExchange,
  //兑换限制生效时间
  exchangeStartTime,
  //兑换限制失效时间
  exchangeEndTime,
  //是否区分用户身份：1-全部 2-指定会员类型
  userFlag,
  //会员ID集合
  memberIdList,
  //有效期生效时间
  validStartTime,
  //有效期失效时间
  validEndTime,
  //排序值
  sort
) {
  return request({
    url: '/b2b/api/v1/integralExchangeGoods/save',
    method: 'post',
    data: {
      id,
      goodsId,
      goodsName,
      goodsType,
      exchangeUseIntegral,
      canExchangeNum,
      singleMaxExchange,
      exchangeStartTime,
      exchangeEndTime,
      userFlag,
      memberIdList,
      validStartTime,
      validEndTime,
      sort
    }
  })
}
//积分兑换商品查看详情
export function integralExchangeGoodsGet(
  //兑换id
  id
) {
  return request({
    url: '/b2b/api/v1/integralExchangeGoods/get',
    method: 'get',
    params: {
      id
    }
  })
}
//积分发放/扣减记录分页列表
export function integralRecordListPage(
  //类型：1-发放 2-扣减
  type,
  //规则名称	
  ruleName,
  //行为名称	
  behaviorName,
  //手机号	
  mobile,
  //发放/扣减开始时间	
  startOperTime,
  //发放/扣减结束时间	
  endOperTime,
  //用户ID	
  uid,
  //用户名称	
  uname,
  //备注
  opRemark,
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/integralRecord/queryListPage',
    method: 'post',
    data: {
      type,
      ruleName,
      behaviorName,
      mobile,
      startOperTime,
      endOperTime,
      uid,
      uname,
      opRemark,
      current,
      size
    }
  })
}
//积分兑换订单分页列表
export function integralQueryListPage(
  //用户ID
  uid,
  //用户名称
  uname,
  //开始兑换提交时间
  startSubmitTime,
  //结束兑换提交时间
  endSubmitTime,
  //兑换订单号
  orderNo,
  //物品名称
  goodsName,
  //开始订单兑付时间
  startExchangeTime,
  //结束订单兑付时间
  endExchangeTime,
  //兑换状态：1-未兑换 2-已兑换
  status,
  //提交人手机号
  mobile,
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/integralExchangeOrder/queryListPage',
    method: 'post',
    data: {
      uid,
      uname,
      startSubmitTime,
      endSubmitTime,
      orderNo,
      goodsName,
      startExchangeTime,
      endExchangeTime,
      status,
      mobile,
      current,
      size
    }
  })
}
//查询积分订单明细（订单送积分）
export function getOrderDetailByRecordId(
  id
) {
  return request({
    url: '/b2b/api/v1/integralRecord/getOrderDetailByRecordId',
    method: 'get',
    params: {
      id
    }
  })
}
//查询签到明细（签到送积分）
export function getSignDetailByRecordId(
  id
) {
  return request({
    url: '/b2b/api/v1/integralRecord/getSignDetailByRecordId',
    method: 'get',
    params: {
      id
    }
  })
}
//兑付详情
export function exchangeDetail(
  id
) {
  return request({
    url: '/b2b/api/v1/integralExchangeOrder/exchangeDetail',
    method: 'get',
    params: {
      id
    }
  })
}
//获取积分说明配置
export function ructionConfigGet(
  
) {
  return request({
    url: '/b2b/api/v1/integralInstructionConfig/get',
    method: 'get',
    params: {
      
    }
  })
}
//保存积分说明配置
export function saveConfig(
  content,
	id
) {
    return request({
      url: '/b2b/api/v1/integralInstructionConfig/saveConfig',
      method: 'post',
      data: {
        content,
        id
      }
    })
  }
//积分兑换商品上下架
export function updateShelfStatus(
  id,
  //上架状态：1-已上架 2-已下架
	status
) {
    return request({
      url: '/b2b/api/v1/integralExchangeGoods/updateShelfStatus',
      method: 'post',
      data: {
        id,
        status
      }
    })
  }
// 积分兑换消息配置分页
export function queryMessageListPage(
  //名称
  title,
  //状态：1-启用 2-禁用
  status,
  //创建时间开始
  startCreateTime,
  //创建时间结束
  endCreateTime,
  //投放开始时间开始
  startTimeStart,
  //投放开始时间结束
  startTimeEnd,
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/integralExchangeMessage/queryListPage',
    method: 'post',
    data: {
      title,
      status,
      startCreateTime,
      endCreateTime,
      startTimeStart,
      startTimeEnd,
      current,
      size
    }
  })
}
//保存积分兑换消息配置
export function saveConfigMessage(
  id,
  //名称
  title,
  //图标	
  icon,
  //投放开始时间
  startTime,
  //投放结束时间
  endTime,
  //排序
  sort,
  //状态
  status,
  //页面配置：1-活动链接
  pageConfig,
  //超链接
  link
) {
  return request({
    url: '/b2b/api/v1/integralExchangeMessage/saveConfig',
    method: 'post',
    data: {
      id,
      title,
      icon,
      startTime,
      endTime,
      sort,
      status,
      pageConfig,
      link
    }
  })
}
//积分兑换 消息 查看
export function messageGet(
  id	
) {
  return request({
    url: '/b2b/api/v1/integralExchangeMessage/get',
    method: 'get',
    params: {
      id
    }
  })
}
//积分兑换 消息 启用禁用
export function messageUpdateStatus(
  id,
  //状态：1-启用 2-禁用
  status
) {
  return request({
    url: '/b2b/api/v1/integralExchangeMessage/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}
//积分兑换 消息 删除
export function messageDeleteConfig(
  id
) {
  return request({
    url: '/b2b/api/v1/integralExchangeMessage/deleteConfig',
    method: 'post',
    data: {
      id
    }
  })
}
//积分兑换 消息 排序
export function messageUpdateOrder(
  id,
  //排序
  sort
) {
  return request({
    url: '/b2b/api/v1/integralExchangeMessage/updateOrder',
    method: 'post',
    data: {
      id,
      sort
    }
  })
}
//订单兑付 详情修改
export function dfUpdate(
  //兑付订单
  id,
  //收货人	
  contactor,
  //联系电话	
  contactorPhone,
  //省
  provinceCode,
  //市
  cityCode,
  //区
  regionCode,
  //详细地址
  address,
  //快递公司
  expressCompany,
  //快递单号
  expressOrderNo
) {
  return request({
    url: '/b2b/api/v1/integralExchangeOrder/update',
    method: 'post',
    data: {
      id,
      contactor,
      contactorPhone,
      provinceCode,
      cityCode,
      regionCode,
      address,
      expressCompany,
      expressOrderNo
    }
  })
}
// 修改地址
export function updateAddress(
  //兑付订单
  id,
  //收货人	
  contactor,
  //联系电话	
  contactorPhone,
  //省
  provinceCode,
  //市
  cityCode,
  //区
  regionCode,
  //详细地址
  address
) {
  return request({
    url: '/b2b/api/v1/integralExchangeOrder/updateAddress',
    method: 'post',
    data: {
      id,
      contactor,
      contactorPhone,
      provinceCode,
      cityCode,
      regionCode,
      address
    }
  })
}
//立即兑付
export function atOnceExchange(
  //兑付订单ID（单个兑付）
  id,
  //快递单号
  expressCompany,
  //订单ID
  expressOrderNo
) {
  return request({
    url: '/b2b/api/v1/integralExchangeOrder/atOnceExchange',
    method: 'post',
    data: {
      id,
      expressCompany,
      expressOrderNo
    }
  })
}