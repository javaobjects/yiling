import request from '@/subject/admin/utils/request';

//查询sql
export function querySql(
  sqlContext,
  sqlCount,
  suId
){
  return request({
    url: '/erp/api/v1/command/sql/query',
    method: 'post',
    data: {
      sqlContext,
      sqlCount,
      suId
    },
    timeout: 60000
  })
}
// 获取系统信息 
export function systeMassage(
  suId //id
){
  return request({
    url: '/erp/api/v1/command/task/list',
    method: 'get',
    params: {
      suId
    }
  })
}
// 点击系统信息
export function taskGet(
  suId, //id
  taskNo //系统信息 id
){
  return request({
    url: '/erp/api/v1/command/task/get',
    method: 'post',
    data: {
      suId,
      taskNo
    }
  })
}
// 保存任务中 保存
export function taskSave(
  createTime,
	flowDateCount,
	methodName,
	springId,
	suId,
	syncStatus,
	syncTime,
	taskInterval,
	taskKey,
	taskName,
	taskNo,
	taskSql,
	taskStatus,
	updateTime
){
  return request({
    url: '/erp/api/v1/command/task/save',
    method: 'post',
    data: {
      createTime,
      flowDateCount,
      methodName,
      springId,
      suId,
      syncStatus,
      syncTime,
      taskInterval,
      taskKey,
      taskName,
      taskNo,
      taskSql,
      taskStatus,
      updateTime
    },
    timeout: 60000
  })
}
// 清除缓存数据
export function deleteCache(
  //商业编号
	suId,
  //任务编号
	taskNo
){
  return request({
    url: '/erp/api/v1/command/task/deleteCache',
    method: 'post',
    data: {
      suId,
      taskNo
    },
    timeout: 60000
  })
}
// 执行sql
export function sqlExecute(
  //流向数据天数
  flowDateCount,
  //商业编号
	suId,
  //任务编号
  taskNo,
  //任务sql
  taskSql 
){
  return request({
    url: '/erp/api/v1/command/task/sqlExecute',
    method: 'post',
    data: {
      flowDateCount,
      suId,
      taskNo,
      taskSql
    },
    timeout: 60000
  })
}
// 版本升级
export function clientUpdate(
  //商业编号
  suId
){
  return request({
    url: '/erp/api/v1/command/client/update',
    method: 'post',
    data: {
      suId
    },
    timeout: 60000
  })
}
// 版本升级日志输出
export function updateLog(
  //商业编号
	suId
){
  return request({
    url: '/erp/api/v1/command/client/updateLog',
    method: 'post',
    data: {
      suId
    },
    timeout: 60000
  })
}

// 开启远程执行
export function updateComand(
	suId //商业编号
){
  return request({
    url: '/erp/api/v1/erpMonitor/updateComand',
    method: 'post',
    data: {
      suId
    }
  })
}