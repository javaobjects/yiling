import request from '@/subject/admin/utils/request'

// 系统日志分页列表
export function getSystemLogList(
  current,//  页码
  size,//  每页记录数
  status,//  操作状态
  // requestMethod,//  请求方法
  title,//  请求标题
  // businessType, //  业务类型
  systemId,//  系统标识
  errorMsg,//  错误信息
  // requestData,//  请求数据
  // responseData,//  响应数据
  requestId, //traceId
  requestUrl, //请求url
  operId, //操作人ID
  startOpTime, //开始操作时间（精确到秒）
  endOpTime //结束操作时间（精确到秒）
) {
  return request({
    url: '/system/api/v1/sysOperLog/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      status,
      // requestMethod,
      title,
      // businessType,
      systemId,
      errorMsg,
      // requestData,
      // responseData,
      requestId,
      requestUrl,
      operId,
      startOpTime,
      endOpTime
    }
  });
}

// 登录日志分页列表
export function getLoginLogList(
  current,//  页码
  size,//  每页记录数
  appId,//  应用id
  // userName,//  用户名
  startLoginTime,//  开始登录时间
  endLoginTime,//  结束登录时间
  userId, //用户ID
  loginAccount //登录账号
) {
  return request({
    url: '/system/api/v1/sysLoginLog/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      appId,
      // userName,
      startLoginTime,
      endLoginTime,
      userId,
      loginAccount
    }
  });
}
// 短信日志列表 
export function smsPageList(
  current,//  页码
  mobile, //接收人手机号
  size,//  每页记录数
  status, //发送状态：1-待发送 2-发送成功 3-发送失败
  startCreateTime, //开始创建时间
  endCreateTime //结束创建时间
) {
  return request({
    url: '/system/api/v1/sms/record/pageList',
    method: 'post',
    data: {
      current,
      mobile,
      size,
      status,
      startCreateTime, 
      endCreateTime 
    }
  });
}