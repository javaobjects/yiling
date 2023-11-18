import request from '@/subject/admin/utils/request'
// 积分管理
//积分发放规则分页
export function queryListPage(
  //规则名称
  name,
  //执行状态：1-启用 2-停用
  status,
  //执行进度：1-未开始 2-进行中 3-已结束
  progress,
  //创建人名称
  createUserName,
  //创建人手机号
  mobile,
  //规则生效时间
  startTime,
  //规则失效时间
  endTime,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralGiveRule/queryListPage',
    method: 'post',
    data: {
      name,
      status,
      progress,
      createUserName,
      mobile,
      startTime,
      endTime,
      current,
      size
    }
  })
}
//选择行为分页列表
export function behaviorListPage(
  //平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手（见字典：integral_rule_platform）
  platform,
  //行为类型：1-发放 2-消耗
  type,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralBehavior/queryListPage',
    method: 'post',
    data: {
      platform,
      type,
      current,
      size
    }
  })
}
//保存积分发放规则基本信息
export function saveBasic(
  //规则名称
  name,
  //规则生效时间
  startTime,
  //规则失效时间
  endTime,
  //规则说明
  description,
  //行为ID
  behaviorId,
  id
){
  return request({
    url: '/b2b/api/v1/integralGiveRule/saveBasic',
    method: 'post',
    data: {
      name,
      startTime,
      endTime,
      description,
      behaviorId,
      id
    }
  })
}
//签到送积分保存
export function saveSignPeriod(
  //发放规则ID
  giveRuleId,
  //积分周期配置集合
  integralPeriodConfigList,
  //签到周期
  signPeriod
) {
  return request({
    url: '/b2b/api/v1/integralGiveRule/saveSignPeriod',
    method: 'post',
    data: {
      giveRuleId,
      integralPeriodConfigList,
      signPeriod
    }
  })
}
//停用发放规则
export function updateStatus(
  //规则id
  id
){
  return request({
    url: '/b2b/api/v1/integralGiveRule/updateStatus',
    method: 'get',
    params: {
      id
    }
  })
}
//查看规则
export function getDistributionRules(
  //规则id
  id
) {
  return request({
    url: '/b2b/api/v1/integralGiveRule/get',
    method: 'get',
    params: {
      id
    }
  })
}
// 订单送积分-指定商家-添加商家
export function giveSellerScope(
  //企业id-单独添加时使用
  eid,
  //企业id-单独添加时使用
	eidList,
  //发放规则ID
	giveRuleId
) {
  return request({
    url: '/b2b/api/v1/integralGiveSellerScope/add',
    method: 'post',
    data: {
      eid,
      eidList,
      giveRuleId
    }
  })
}
// 订单送积分-指定商家-已添加商家分页列表查询
export function pageList(
  //企业id
  eid,
  //企业名称
  ename,
  //所属省份编码
  provinceCode,
  //所属城市编码
  cityCode,
  //所属区域编码
  regionCode,
  //发放规则ID	
  giveRuleId,
  current,
  size,
  //企业标签
  tagIds
) {
  return request({
    url: '/b2b/api/v1/integralGiveSellerScope/pageList',
    method: 'post',
    data: {
      eid,
      ename,
      provinceCode,
      cityCode,
      regionCode,
      giveRuleId,
      current,
      size,
      tagIds
    }
  })
}
//订单送积分-指定商家-删除商家
export function giveSellerScopeDelete(
  //企业id-单独添加时使用
  eid,
  //企业id-单独添加时使用
	eidList,
  //发放规则ID
	giveRuleId
) {
  return request({
    url: '/b2b/api/v1/integralGiveSellerScope/delete',
    method: 'post',
    data: {
      eid,
      eidList,
      giveRuleId
    }
  })
}
//订单送积分-平台SKU接口分页列表查询
export function givePlatformGoodsList(
  giveRuleId,
  //商品id
  standardId,
  //	规格ID-精确搜索
  sellSpecificationsId,
  //	商品名称-模糊搜索
  goodsName,
  //生产厂家-模糊搜索
  manufacturer,
  //以岭品 0-全部 1-是 2-否
  isYiLing,
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/integralGivePlatformGoods/list',
    method: 'post',
    data: {
      giveRuleId,
      standardId,
      sellSpecificationsId,
      goodsName,
      manufacturer,
      isYiLing,
      current,
      size
    }
  })
}
//订单送积分-已添加平台SKU分页列表查询
export function givePlatformGoodsPageList(
  giveRuleId,
  //商品id
  standardId,
  //	规格ID-精确搜索
  sellSpecificationsId,
  //	商品名称-模糊搜索
  goodsName,
  //生产厂家-模糊搜索
  manufacturer,
  //以岭品 0-全部 1-是 2-否
  isYiLing,
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/integralGivePlatformGoods/pageList',
    method: 'post',
    data: {
      giveRuleId,
      standardId,
      sellSpecificationsId,
      goodsName,
      manufacturer,
      isYiLing,
      current,
      size
    }
  })
}
// 订单送积分-添加平台SKU
export function addGivePlatformGoods(
  //发放规则ID
  giveRuleId,
  //商品ID-精确搜索
  standardIdPage,
  //规格ID-精确搜索
  sellSpecificationsIdPage,
  //商品名称-模糊搜索
  goodsNamePage,
  //生产厂家-模糊搜索
  manufacturerPage,
  //以岭品 0-全部 1-是 2-否
  isYiLing,
  //规格ID-添加当前页时使用
  sellSpecificationsIdList
){
  return request({
    url: '/b2b/api/v1/integralGivePlatformGoods/add',
    method: 'post',
    data: {
      giveRuleId,
      standardIdPage,
      sellSpecificationsIdPage,
      goodsNamePage,
      manufacturerPage,
      isYiLing,
      sellSpecificationsIdList
    }
  })
}
//订单送积分-删除平台SKU
export function deleteGivePlatformGoods(
  //发放规则ID
  giveRuleId,
  //商品ID-精确搜索
  standardIdPage,
  //规格ID-精确搜索
  sellSpecificationsIdPage,
  //商品名称-模糊搜索
  goodsNamePage,
  //生产厂家-模糊搜索
  manufacturerPage,
  //以岭品 0-全部 1-是 2-否
  isYiLing,
  //规格ID-添加当前页时使用
  sellSpecificationsIdList
){
  return request({
    url: '/b2b/api/v1/integralGivePlatformGoods/delete',
    method: 'post',
    data: {
      giveRuleId,
      standardIdPage,
      sellSpecificationsIdPage,
      goodsNamePage,
      manufacturerPage,
      isYiLing,
      sellSpecificationsIdList
    }
  })
}
//订单送积分-店铺SKU-待添加店铺SKU分页列表查询
export function giveEnterpriseGoodsList(
  //发放规则ID
  giveRuleId,
  //商家范围类型（1-全部商家；2-指定商家；）
  conditionSellerType,
  //商品名称
  goodsName,
  //商品ID-精确搜索
  goodsId,
  //企业名称-模糊搜索
  ename,
  //以岭品 0-全部 1-是 2-否
  yilingGoodsFlag,
  //商品状态：1上架 2下架 3待设置
  goodsStatus,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralGiveEnterpriseGoods/list',
    method: 'post',
    data: {
      giveRuleId,
      conditionSellerType,
      goodsName,
      goodsId,
      ename,
      yilingGoodsFlag,
      goodsStatus,
      current,
      size
    }
  })
}
//订单送积分-店铺SKU-添加店铺SKU
export function addGiveEnterpriseGoods(
  //发放规则ID
  giveRuleId,
  //商家范围：1-全部商家 2-指定商家
  merchantScope,
  //商品名称-模糊搜索
  goodsNamePage,
  //商品ID-精确搜索
  goodsIdPage,
  //企业名称-模糊搜索
  enamePage,
  //以岭品 0-全部 1-是 2-否
  yilingGoodsFlag,
  //商品状态：1上架 2下架 3待设置
  goodsStatus,
  //商品id-单独添加时使用
  goodsId,
  //商品id集合-添加当前页时使用
  goodsIdList
){
  return request({
    url: '/b2b/api/v1/integralGiveEnterpriseGoods/add',
    method: 'post',
    data: {
      giveRuleId,
      merchantScope,
      goodsNamePage,
      goodsIdPage,
      enamePage,
      yilingGoodsFlag,
      goodsStatus,
      goodsId,
      goodsIdList
    }
  })
}
//订单送积分-店铺SKU-已添加店铺SKU分页列表查询
export function giveEnterpriseGoodsPageList(
  //发放规则ID
  giveRuleId,
  //商家范围类型（1-全部商家；2-指定商家；）
  conditionSellerType,
  //商品名称
  goodsName,
  //商品ID-精确搜索
  goodsId,
  //企业名称-模糊搜索
  ename,
  //以岭品 0-全部 1-是 2-否
  yilingGoodsFlag,
  // //商品状态：1上架 2下架 3待设置
  // goodsStatus,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralGiveEnterpriseGoods/pageList',
    method: 'post',
    data: {
      giveRuleId,
      conditionSellerType,
      goodsName,
      goodsId,
      ename,
      yilingGoodsFlag,
      // goodsStatus,
      current,
      size
    }
  })
}
//订单送积分-店铺SKU-删除店铺SKU
export function deleteGiveEnterpriseGoods(
  //发放规则ID
  giveRuleId,
  //商品名称-模糊搜索
  goodsNamePage,
  //商品ID-精确搜索
  goodsIdPage,
  //企业名称-模糊搜索
  enamePage,
  //以岭品 0-全部 1-是 2-否
  yilingGoodsFlag,
  //商品id-单独添加时使用
  goodsId,
  //商品id集合-添加当前页时使用
  goodsIdList
){
  return request({
    url: '/b2b/api/v1/integralGiveEnterpriseGoods/delete',
    method: 'post',
    data: {
      giveRuleId,
      goodsNamePage,
      goodsIdPage,
      enamePage,
      yilingGoodsFlag,
      goodsId,
      goodsIdList
    }
  })
}
//订单送积分-选择需添加客户列表查询
export function giveEnterprisePage(
  //企业ID
  id,
  //企业名称
  name,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralGiveEnterprise/page',
    method: 'post',
    data: {
      id,
      name,
      current,
      size
    }
  })
}
//订单送积分-已添加客户列表查询
export function giveEnterprisePageList(
  //发放规则id
  giveRuleId,
  //企业ID
  eid,
  //企业名称
  ename,
  current,
  size
){
  return request({
    url: '/b2b/api/v1/integralGiveEnterprise/pageList',
    method: 'post',
    data: {
      giveRuleId,
      eid,
      ename,
      current,
      size
    }
  })
}
//订单送积分-添加客户
export function giveEnterpriseAdd(
  //发放规则ID
  giveRuleId,
  //企业id-单独添加时使用
  eid,
  //企业id集合-添加当前页时使用
  eidList,
  //企业ID-精确搜索-添加搜索结果时使用
  eidPage,
  //企业名称-模糊搜索-添加搜索结果时使用
  enamePage
) {
  return request({
    url: '/b2b/api/v1/integralGiveEnterprise/add',
    method: 'post',
    data: {
      giveRuleId,
      eid,
      eidList,
      eidPage,
      enamePage
    }
  })
}
//订单送积分-删除客户
export function giveEnterpriseDelete(
  //发放规则ID
  giveRuleId,
  //企业id-单独添加时使用
  eid,
  //企业id集合-添加当前页时使用
  eidList,
  //企业ID-精确搜索-添加搜索结果时使用
  eidPage,
  //企业名称-模糊搜索-添加搜索结果时使用
  enamePage
) {
  return request({
    url: '/b2b/api/v1/integralGiveEnterprise/delete',
    method: 'post',
    data: {
      giveRuleId,
      eid,
      eidList,
      eidPage,
      enamePage
    }
  })
}
//订单送积分-添加会员方案
export function giveMemberPage(
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/integralGiveMember/page',
    method: 'post',
    data: {
      current,
      size
    }
  })
}
//订单送积分-添加会员
export function giveMemberAdd(
  //发放规则ID
  giveRuleId,
  //会员ID
  memberId
) {
  return request({
    url: '/b2b/api/v1/integralGiveMember/add',
    method: 'post',
    data: {
      giveRuleId,
      memberId
    }
  })
}
//订单送积分-已添加的指定会员方案分页列表查询
export function giveMemberPageList(
  //发放规则ID
  giveRuleId,
  current,
  size
) {
  return request({
    url: '/b2b/api/v1/integralGiveMember/pageList',
    method: 'post',
    data: {
      giveRuleId,
      current,
      size
    }
  })
}
//订单送积分-删除会员方案
export function giveMemberDelete(
  //发放规则ID
  giveRuleId,
  //会员ID
  memberId
) {
  return request({
    url: '/b2b/api/v1/integralGiveMember/delete',
    method: 'post',
    data: {
      giveRuleId,
      memberId
    }
  })
}
//保存订单送积分
export function saveOrderGiveIntegral(
  //发放规则ID
  giveRuleId,
  //商家范围：1-全部商家 2-指定商家
  merchantScope,
  //商品范围：1-全部商品 2-指定平台SKU 3-指定店铺SKU
  goodsScope,
  //客户范围：1-全部客户 2-指定客户 3-指定客户范围
  customerScope,
  //指定客户范围的企业类型：1-全部类型 2-指定类型
  enterpriseType,
  //指定范围企业类型集合（企业类型：3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所，参考企业类型的字典即可）
  enterpriseTypeList,
  //指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员
  userType,
  //是否区分支付方式：1-全部支付方式 2-指定支付方式	
  paymentMethodFlag,
  //支付方式：1-线上支付 2-线下支付 3-账期支付
  paymentMethodList
) {
  return request({
    url: '/b2b/api/v1/integralGiveRule/saveOrderGiveIntegral',
    method: 'post',
    data: {
      giveRuleId,
      merchantScope,
      goodsScope,
      customerScope,
      enterpriseType,
      enterpriseTypeList,
      userType,
      paymentMethodFlag,
      paymentMethodList
    }
  })
}
//生成订单积分倍数配置表格
export function generateMultipleConfig(
  //发放规则ID
  giveRuleId
) {
  return request({
    url: '/b2b/api/v1/integralGiveRule/generateMultipleConfig',
    method: 'get',
    params: {
      giveRuleId
    }
  })
}
//保存订单积分倍数配置
export function saveMultipleConfig(
  //发放规则ID
  giveRuleId,
  //积分倍数配置集合
  multipleConfigList
) {
  return request({
    url: '/b2b/api/v1/integralGiveRule/saveMultipleConfig',
    method: 'post',
    data: {
      giveRuleId,
      multipleConfigList
    }
  })
}
//复制
export function integralGiveRuleCopy(
  //发放规则ID
  id	
) {
  return request({
    url: '/b2b/api/v1/integralGiveRule/copy',
    method: 'get',
    params: {
      id
    }
  })
}
//获取企业标签选择项列表
export function selectTags() {
  return request({
    url: '/b2b/api/v1/integralGiveSellerScope/selectTags',
    method: 'get',
    params: {}
  })
}