import request from '@/subject/admin/utils/request';
// erp 管理
export function abnormalCustomerList(
  current, //第几页，默认：1
	errorCode, //错误code
	licenseNo, //唯一代码
	name, //终端名称
	size, //每页记录数，默认：10
	suId //供应商id
	// syncStatus //同步状态
){
  return request({
    url: '/erp/api/v1/erp/customer/abnormalCustomerList',
    method: 'post',
    data: {
      current,
      errorCode,
      licenseNo,
      name,
      size,
      suId
      // syncStatus
    }
  })
}
// id查询erp客户
export function findById(
  erpCustomerId
){
  return request({
    url: '/erp/api/v1/erp/customer/findById',
    method: 'get',
    params: {
      erpCustomerId
    }
  })
}
//维护erp客户
export function maintain(
  // customerEid,
	erpCustomer = {},
	tycEnterprise = {}
){
  return request({
    url: '/erp/api/v1/erp/customer/maintain',
    method: 'post',
    data: {
      // customerEid,
      erpCustomer,
      tycEnterprise
    }
  })
}
// 手动绑定平台企业
export function erpBind(
  // customerEid,
	erpCustomer = {},
	tycEnterprise = {}
){
  return request({
    url: '/erp/api/v1/erp/customer/bind',
    method: 'post',
    data: {
      // customerEid,
      erpCustomer,
      tycEnterprise
    }
  })
}
// 名字模糊查询接通erp的供应商
export function erpSupplierList(
  name
){
  return request({
    url: '/erp/api/v1/erp/customer/erpSupplierList',
    method: 'get',
    params: {
      name
    }
  })
}
// 名字分页模糊查询企业
export function enterpriseList(
  current, //第几页，默认：1
	name, //终端名称
	size //每页记录数，默认：1
){
  return request({
    url: '/erp/api/v1/erp/customer/enterpriseList',
    method: 'post',
    data: {
      current,
      name,
      size
    }
  })
}
// 天眼查查询企业
export function queryByKeyword(
  keyword
){
  return request({
    url: '/erp/api/v1/erp/customer/tyc/queryByKeyword',
    method: 'get',
    params: {
      keyword
    }
  })
}
// 手动绑定平台企业
export function bind(
  customerEid,
  erpCustomer = {}
  // tycEnterprise = {}
){
  return request({
    url: '/erp/api/v1/erp/customer/bind',
    method: 'post',
    data: {
      customerEid,
      erpCustomer
      // tycEnterprise
    }
  })
}
// 手动绑定天眼查企业
export function bindTyc(
  erpCustomer = {},
  tycEnterprise = {}
){
  return request({
    url: '/erp/api/v1/erp/customer/bindTyc',
    method: 'post',
    data: {
      erpCustomer,
      tycEnterprise
    }
  })
}

// 对接企业管理
export function queryListPage(
  //企业名称
  clientName,
  //第几页，默认：1
	current,
  //对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
	depth,
  //流向级别：0-未对接 1-以岭流向 2-全品流向
	flowLevel,
  //对接负责人
	installEmployee,
  //每页记录数，默认：10
	size,
  //同步状态：0-未开启 1-开启
	syncStatus,
  //监控状态：0-未开启 1-开启，-1 全部
  monitorStatus,
  //终端激活状态：-1 全部，0-未激活 1-已激活
  clientStatus,
  //crmID
  crmEnterpriseId
){
  return request({
    url: '/erp/api/v1/erpEnterprise/queryListPage',
    method: 'post',
    data: {
      clientName,
      current,
      depth,
      flowLevel,
      installEmployee,
      size,
      syncStatus,
      monitorStatus,
      clientStatus,
      crmEnterpriseId
    }
  })
}
// 查询企业信息
export function queryParentListPage(
  clientName, //企业名称
	current, //第几页，默认：1
	// depth, //对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
	// flow, //流向级别：0-未对接 1-以岭流向 2-全品流向
	// installEmployee, //对接负责人
	rkSuId, //企业ID
	size //每页记录数，默认：10
	// syncStatus //同步状态：0-未开启 1-开启
){
  return request({
    url: '/erp/api/v1/erpEnterprise/queryParentListPage',
    method: 'post',
    data: {
      clientName,
      current,
      // depth,
      // flow,
      // installEmployee,
      rkSuId,
      size
      // syncStatus
    }
  })
}
// 查询企业信息
export function queryEnterpriseList(
  name
){
  return request({
    url: '/erp/api/v1/erpEnterprise/queryEnterpriseList',
    method: 'post',
    data: {
      name
    }
  })
}
// 新增
export function erpSave(
  //商务人员
  businessEmployee,
  //远程执行命令：0-执行完成 1-远程更新版本 2-重启服务
	// command,
  //对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
	depth,
  //erp品牌
	erpBrand,
  //流向级别：0-未对接 1-以岭流向 2-全品流向
	flowLevel,
  //对接负责人
	installEmployee,
  //备注
	remark,
  //企业ID
	rkSuId,
  //分公司编码
	suDeptNo,
  //父级企业id
	suId,
  //同步状态：0-未开启 1-开启
	syncStatus,
  // 工具对接方式
  flowMode,
  //终端激活状态 0-未激活 1-已激活
  clientStatus,
  //crm企业ID
  crmEnterpriseId,
  //负责人工号
  installEmpId,
  //月流向采集时间
  flowMonthCollectTime
){
  return request({
    url: '/erp/api/v1/erpEnterprise/save',
    method: 'post',
    data: {
      businessEmployee,
      // command,
      depth,
      erpBrand,
      flowLevel,
      // id,
      installEmployee,
      remark,
      rkSuId,
      suDeptNo,
      suId,
      syncStatus,
      flowMode,
      clientStatus,
      crmEnterpriseId,
      installEmpId,
      flowMonthCollectTime
    }
  })
}
//对接企业 详情
export function queryDetail(
  rkSuId
){
  return request({
    url: '/erp/api/v1/erpEnterprise/queryDetail',
    method: 'get',
    params: {
      rkSuId
    }
  })
}
//
export function queryUpdate(
  //商务人员
  businessEmployee,
  //远程执行命令：0-执行完成 1-远程更新版本 2-重启服务
	// command,
  //对接级别：0-未对接 1-基础对接 2-订单提取 3-发货单对接
	depth,
  //erp品牌
	erpBrand,
  //流向级别：0-未对接 1-以岭流向 2-全品流向
	flowLevel,
  //ID
	id,
  //对接负责人 
	installEmployee,
  //备注 
	remark,
  //企业ID 
	rkSuId,
  //分公司编码 
	suDeptNo,
  //父级企业id
	suId,
  //同步状态：0-未开启 1-开启
	syncStatus,
  // 工具对接方式
  flowMode,
  //终端激活状态 0-未激活 1-已激活
  clientStatus,
  //crm企业ID
  crmEnterpriseId,
  //负责人工号
  installEmpId,
  //月流向采集时间
  flowMonthCollectTime
){
  return request({
    url: '/erp/api/v1/erpEnterprise/update',
    method: 'post',
    data: {
      businessEmployee,
      // command,
      depth,
      erpBrand,
      flowLevel,
      id,
      installEmployee,
      remark,
      rkSuId,
      suDeptNo,
      suId,
      syncStatus,
      flowMode,
      clientStatus,
      crmEnterpriseId,
      installEmpId,
      flowMonthCollectTime
    }
  })
}
// 开启/关闭监控状态
export function updateMonitorStatus(
  monitorStatus, //监控状态：0-未开启 1-开启
	rkSuId //企业ID
){
  return request({
    url: '/erp/api/v1/erpEnterprise/updateMonitorStatus',
    method: 'post',
    data: {
      monitorStatus,
      rkSuId
    }
  })
}
// ERP监控-分页列表和统计信息
export function erpMonitorList(
  // 第几页
  current, 
  // 页数
  size, 
  // 企业ID
  rkSuId, 
  //	企业名称
  clientName, 
  // 负责人
  installEmployee, 
  // 查询类型 1 关闭对接数量 2 1小时内无心跳对接数量 3 当月未上传销售 4 异常数据 5 采购异常
	openType
){
  return request({
    url: '/erp/api/v1/erpMonitor/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      rkSuId,
      clientName,
      installEmployee,
      openType
    }
  })
}
// ERP监控-统计数量
export function erpMonitorTotal(){
  return request({
    url: '/erp/api/v1/erpMonitor/countStatistics',
    method: 'post',
    data: {}
  })
}

// 流向封存信息列表分页
export function flowSealedQueryListPage(
  current, // 第几页
  size, // 页数
  eid, // 商业ID
  ename, //	商业名称
  type, // 流向类型
	status // 封存状态
){
  return request({
    url: '/erp/api/v1/erpFlowSealed/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename,
      type,
      status
    }
  })
}

// 流向封存-封存
export function erpFlowSealedLock(
  id
){
  return request({
    url: '/erp/api/v1/erpFlowSealed/lock',
    method: 'get',
    params: {
      id
    }
  })
}

// 流向封存-解封
export function erpFlowSealedUnLock(
  id
){
  return request({
    url: '/erp/api/v1/erpFlowSealed/unLock',
    method: 'get',
    params: {
      id
    }
  })
}

// 流向添加封存-月份列表, 仅支持前推6个整月
export function erpFlowSealedMonthList(){
  return request({
    url: '/erp/api/v1/erpFlowSealed/monthList',
    method: 'get',
    params: {}
  })
}

// 流向添加封存-商业公司列表分页
export function flowSealedEnterprisePage(
  current, // 第几页
  size, // 页数
  eid, // 商业ID
  ename //	商业名称
){
  return request({
    url: '/erp/api/v1/erpFlowSealed/enterprisePage',
    method: 'post',
    data: {
      current,
      size,
      eid,
      ename
    }
  })
}

// 流向封存-添加保存
export function flowSealedSave(
  eidList,
  typeList,
  monthList
){
  return request({
    url: '/erp/api/v1/erpFlowSealed/save',
    method: 'post',
    data: {
      eidList,
      typeList,
      monthList
    }
  })
}

// 商品配置信息列表分页
export function FlowGoodsConfigList(
  current, // 第几页
  size, // 页数
  ename, //	商业名称
  eid, // 商业ID
  goodsName,
  goodsInSn,
  startCreateTime,
  endCreateTime
){
  return request({
    url: '/erp/api/v1/erpFlowGoodsConfig/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      ename,
      eid,
      goodsName,
      goodsInSn,
      startCreateTime,
      endCreateTime
    }
  })
}

// 查询商业公司ID
export function getEnterpriseId(
  ename
){
  return request({
    url: '/erp/api/v1/erpFlowGoodsConfig/getEnterpriseId',
    method: 'get',
    params: {
      ename
    }
  })
}

// 流向封存-添加保存
export function flowGoodsConfigSave(
  ename,
  eid,
  specifications,
  goodsName,
  goodsInSn,
  licenseNo,
  manufacturer
){
  return request({
    url: '/erp/api/v1/erpFlowGoodsConfig/save',
    method: 'post',
    data: {
      ename,
      eid,
      specifications,
      goodsName,
      goodsInSn,
      licenseNo,
      manufacturer
    }
  })
}

// 删除
export function flowGoodsConfigDelete(
  id
){
  return request({
    url: '/erp/api/v1/erpFlowGoodsConfig/delete',
    method: 'get',
    params: {
      id
    }
  })
}
// 销售异常信息列表分页
export function querySaleExceptionListPage(
  // 企业名称
  ename,
  // 企业ID
  eid,
  // 销售单主键ID
  soId,
  // 销售单号
  soNo,
  // 销售单据时间，开始
  flowTimeStart,
  // 销售单据时间，结束
  flowTimeEnd,
  // 查询类型 1 关闭对接数量 2 1小时内无心跳对接数量 3 当月未上传销售 4 异常数据 5 采购异常
  openType,
  // 第几页，默认：1
	current,
  // 每页记录数，默认：10
	size
){
  return request({
    url: '/erp/api/v1/erpMonitor/querySaleExceptionListPage',
    method: 'post',
    data: {
      ename,
      eid,
      soId,
      soNo,
      flowTimeStart,
      flowTimeEnd,
      openType,
      current,
      size
    }
  })
}
// 采购异常信息列表分页
export function queryPurchaseExceptionListPage(
  // 采购企业名称
	ename,
  // 采购企业id
	eid,
  // 采购单据时间，开始
	poTimeStart,
  // 采购单据时间，结束	
  poTimeEnd,
  // 查询类型 1 关闭对接数量 2 1小时内无心跳对接数量 3 当月未上传销售 4 异常数据 5 采购异常
  openType,
  current,
	size
) {
  return request({
    url: '/erp/api/v1/erpMonitor/queryPurchaseExceptionListPage',
    method: 'post',
    data: {
      ename,
      eid,
      poTimeStart,
      poTimeEnd,
      openType,
      current,
      size
    }
  })
}

// 连锁总店门店对应分页列表
export function erpShopMappingPage(
  // 第几页
  current,
  // 页数
  size, 
  // 总店企业id
  mainShopEid,
  // 门店企业id
  shopEid
){
  return request({
    url: '/erp/api/v1/erp/shopMapping/page',
    method: 'post',
    data: {
      current,
      size,
      mainShopEid,
      shopEid
    }
  })
}

// 名字分页模糊查询企业
export function erpCustomerEnterpriseList(
  // 第几页
  current,
  // 页数
  size, 
  // 企业指定类型 1.工业 2.商业 3.连锁总店 4.连锁直营 5.连锁加盟 6.单体药房 7.医疗机构 8.诊所
  inTypeList,
  // 企业名
  name
){
  return request({
    url: '/erp/api/v1/erp/customer/enterpriseList',
    method: 'post',
    data: {
      current,
      size,
      inTypeList,
      name
    }
  })
}

// 批量修改对应关系同步状态
export function erpShopMappingUpdateSyncStatusByQuery(
  // 总店企业id
  mainShopEid,
  // 门店企业id
  shopEid, 
  // 同步状态 ： 0-关闭 1-开启
  syncStatus
){
  return request({
    url: '/erp/api/v1/erp/shopMapping/updateSyncStatusByQuery',
    method: 'post',
    data: {
      mainShopEid,
      shopEid,
      syncStatus
    }
  })
}

// 修改对应关系同步状态
export function erpShopMappingUpdateSyncStatus(
  id,
  // 同步状态 ： 0-关闭 1-开启
  syncStatus
){
  return request({
    url: '/erp/api/v1/erp/shopMapping/updateSyncStatus',
    method: 'post',
    data: {
      id,
      syncStatus
    }
  })
}

// 批量删除对应关系
export function erpShopMappingDeleteByQuery(
  // 总店企业id
  mainShopEid,
  // 门店企业id
  shopEid
){
  return request({
    url: '/erp/api/v1/erp/shopMapping/deleteByQuery',
    method: 'post',
    data: {
      mainShopEid,
      shopEid
    }
  })
}

// 删除对应关系
export function erpShopMappingDelete(
  id
){
  return request({
    url: '/erp/api/v1/erp/shopMapping/delete',
    method: 'post',
    data: {
      id
    }
  })
}
//查询crm企业信息
export function queryCrmEnterpriseList(
  name
){
  return request({
    url: '/erp/api/v1/erpEnterprise/queryCrmEnterpriseList',
    method: 'post',
    data: {
      name
    }
  })
}
//根据工号或岗位代码员工相关信息 
export function getByEmpIdOrJobId(
  empId
){
  return request({
    url: '/erp/api/v1/erpEnterprise/getByEmpIdOrJobId',
    method: 'get',
    params: {
      empId
    }
  })
}