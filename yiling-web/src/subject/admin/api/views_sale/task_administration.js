import request from '@/subject/admin/utils/request'
// 获取列表顶部 任务总量 平台任务 企业任务数量
export function getTaskCount() {
  return request({
    url: '/salesAssistant/api/v1/task/getTaskCount',
    method: 'post',
    params: {}
  })
}
// 任务列表 
export function queryTaskListPage(
  //任务名称
  taskName, 
  //创建时间开始
  startcTime, 
  //创建时间结束
  endcTime, 
  //任务开始时间-起
  startTime, 
  //任务开始时间-止
  endTime, 
  //任务结束时间-起
  starteTime, 
  //任务结束时间-止
  endeTime, 
  //任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买
  finishType,
  //状态 0未开始1进行中2已结束3停用
  taskStatus, 
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size, 
  //0:平台任务1：企业任务
  taskType
) {
  return request({
    url: '/salesAssistant/api/v1/task/queryTaskListPage',
    method: 'get',
    params: {
      taskName, 
      startcTime, 
      endcTime, 
      startTime, 
      endTime, 
      starteTime,
      endeTime, 
      finishType,
      taskStatus,
      current,
      size,
      taskType
    }
  })
}
// 创建任务
export function addRenwu({
  //0:平台任务1：企业任务
  taskType, 
  //任务类型-子类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 10-上传资料
  finishType, 
  //任务名称
  taskName, 
  //任务说明
  taskDesc, 
  //部门
  deptIdList,
  //企业类型 多个逗号分隔
  enterpriseType,
  //任务规则
  addTaskRuleList, 
  //配送商
  addTaskDistributorList, 
  //开始时间
  startTime, 
  //结束时间
  endTime, 
  //任务区域集合
  addTaskAreaList, 
  // 佣金政策-阶梯任务传 非阶梯不传
  addCommissionRuleFormList, 
  //任务商品集合
  addTaskGoodsRelationList, 
  //会员
  addTaskMember 
}){
  return request({
    url: '/salesAssistant/api/v1/task/add',
    method: 'post',
    data: {
      taskType, 
      finishType, 
      taskName, 
      taskDesc, 
      deptIdList,
      enterpriseType,
      addTaskRuleList, 
      addTaskDistributorList, 
      startTime,
      endTime,
      addTaskAreaList, 
      addCommissionRuleFormList, 
      addTaskGoodsRelationList,
      addTaskMember
    }
  })
}
// 保存
export function update({
  //任务id
  taskId, 
  //任务名称
  taskName, 
  //任务说明
  taskDesc,
  //部门
  deptIdList,
  //企业类型 多个逗号分隔
  enterpriseType,
  //任务规则
  updateTaskRuleList, 
  //配送商
  updateTaskDistributorList, 
  //开始时间
  startTime, 
  //结束时间
  endTime,
  //任务区域集合
  updateTaskAreaList, 
  // 佣金政策-阶梯任务传 非阶梯不传
  addCommissionRuleFormList, 
  //任务商品集合
  updateTaskGoodsRelationList, 
  //会员
  updateTaskMember 
}){
  return request({
    url: '/salesAssistant/api/v1/task/update',
    method: 'post',
    data: {
      taskId,
      taskName, 
      taskDesc,
      deptIdList,
      enterpriseType,
      updateTaskRuleList, 
      updateTaskDistributorList, 
      startTime,
      endTime,
      updateTaskAreaList, 
      addCommissionRuleFormList, 
      updateTaskGoodsRelationList,
      updateTaskMember
    }
  })
}
// 选取配送商 
export function queryDistributorPage(
  //第几页，默认：1
  current, 
  //企业名称
  name, 
  //每页记录数，默认：10
  size, 
  //1-管控 2-不管控 0-全部
  type 
){
  return request({
    url: '/salesAssistant/api/v1/task/queryDistributorPage',
    method: 'get',
    params: {
      current,
      name,
      size,
      type
    }
  })
}

// 选择商品
export function queryGoodsForAdd(
  // 0-平台任务1-企业任务
  taskType, 
  //第几页，默认：1
  current, 
  //商品id
  goodsId, 
  //商品名称
  goodsName, 
  //生产厂家
  manufacturer, 
  //每页记录数，默认：10
  size 
){
  return request({
    url: '/salesAssistant/api/v1/task/queryGoodsForAdd',
    method: 'get',
    params: {
      taskType,
      current,
      goodsId,
      goodsName,
      manufacturer,
      size
    }
  })
}
// 创建任务-选择会员 
export function queryMember() {
  return request({
    url: '/salesAssistant/api/v1/task/queryMember',
    method: 'get',
    params: {

    }
  })
}
// 创建任务-根据所选会员-选择会员条件
export function queryMemberStage(
  //会员id
  memberId 
) {
  return request({
    url: '/salesAssistant/api/v1/task/queryMemberStage',
    method: 'get',
    params: {
      memberId
    }
  })
}
// 任务停用
export function stopTask(
  id
) {
  return request({
    url: '/salesAssistant/api/v1/task/stopTask',
    method: 'post',
    data: {
      id
    }
  })
}

// 任务追踪-头部
export function getTaskTrace(
  //taskId
  taskId 
) {
  return request({
    url: '/salesAssistant/api/v1/task/getTaskTrace',
    method: 'get',
    params: {
      taskId
    }
  })
}
// 任务追踪-承接人员明细
export function queryTaskUserPage(
  //taskId
  taskId, 
  //第几页 1
  current, 
  //结束时间
  endTime, 
  //每页记录数，默认：10
  size, 
  //开始时间
  startTime,
  //姓名
  name,
  //手机号
  mobile
) {
  return request({
    url: '/salesAssistant/api/v1/task/queryTaskUserPage',
    method: 'get',
    params: {
      taskId,
      current,
      endTime,
      size,
      startTime,
      name,
      mobile
    }
  })
}
// 查询任务详情
export function getDetailById(
  //taskId
  id 
) {
  return request({
    url: '/salesAssistant/api/v1/task/getDetailById',
    method: 'get',
    params: {
      id
    }
  })
}
// 任务追踪-任务品完成进度查看
export function listTaskTraceGoods(
  userTaskId 
) {
  return request({
    url: '/salesAssistant/api/v1/task/listTaskTraceGoods',
    method: 'get',
    params: {
      userTaskId
    }
  })
}
// 任务追踪-详情页-交易额交易量销售记录
export function listTaskTraceOrderPage(
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size, 
  //userTaskId
  userTaskId 
) {
  return request({
    url: '/salesAssistant/api/v1/task/listTaskTraceOrderPage',
    method: 'get',
    params: {
      current,
      size,
      userTaskId
    }
  })
}
// 任务追踪-详情页-拉户类
export function listTaskTraceTerminalPage(
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size, 
  //userTaskId
  userTaskId 
) {
  return request({
    url: '/salesAssistant/api/v1/task/listTaskTraceTerminalPage',
    method: 'get',
    params: {
      current,
      size,
      userTaskId
    }
  })
}
// 任务追踪-详情页-拉人类
export function listTaskRegisterUserPage(
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size, 
  //userTaskId
  userTaskId 
) {
  return request({
    url: '/salesAssistant/api/v1/task/listTaskRegisterUserPage',
    method: 'get',
    params: {
      current,
      size,
      userTaskId
    }
  })
}
// 任务追踪-会员
export function listTaskMemberPage(
  //第几页，默认：1
  current, 
  //每页记录数，默认：10
  size, 
  //userTaskId
  userTaskId 
) {
  return request({
    url: '/salesAssistant/api/v1/task/listTaskMemberPage',
    method: 'get',
    params: {
      current,
      size,
      userTaskId
    }
  })
}
// 任务区域反显
export function queryTaskArea(
  taskId
) {
  return request({
    url: '/salesAssistant/api/v1/task/queryTaskArea',
    method: 'get',
    params: {
      taskId
    }
  })
}
// 任务详情-配送商反显
export function queryTaskDistributorPage(
  taskId,
  current,
  size,
  type
) {
  return request({
    url: '/salesAssistant/api/v1/task/queryTaskDistributorPage',
    method: 'get',
    params: {
      taskId,
      current,
      size,
      type
    }
  })
}
// 移除任务商品 
export function deleteGoods(
  taskGoodsId
) {
  return request({
    url: '/salesAssistant/api/v1/task/deleteGoods',
    method: 'post',
    data: {
      taskGoodsId
    }
  })
}
// 移除任务所选配送商
export function deleteTaskDistributor(
  taskDistributorId
) {
  return request({
    url: '/salesAssistant/api/v1/task/deleteTaskDistributor',
    method: 'post',
    data: {
      taskDistributorId
    }
  })
}
// 删除任务
export function deleteTask(id) {
  return request({
    url: '/salesAssistant/api/v1/task/deleteTask',
    method: 'post',
    data: {
      id
    }
  })
}
//部门树
export function queryDeptTree() {
  return request({
    url: '/salesAssistant/api/v1/task/queryDeptTree',
    method: 'get',
    params: {}
  })
}
// 任务追踪-随货同行单-销售记录
export function queryTaskAccompanyBillPage(
  //用户任务id
  userTaskId,
  //第几页，默认：1
  current,
  //每页记录数，默认：10
  size
) {
  return request({
    url: '/salesAssistant/api/v1/accompanyingBill/queryTaskAccompanyBillPage',
    method: 'get',
    params: {
      userTaskId,
      current,
      size
    }
  })
}