import request from '@/subject/admin/utils/request'
import { getRandomCode } from '@/subject/admin/utils/index'

// 获取地区级联选择器
export function getLocation(parentCode) {
  return request({
    url: '/system/api/v1/location/listByParentCode',
    method: 'get',
    params: {
      parentCode
    }
  })
}

// 获取区域树结构列表
export function getTreeLocation(deep) {
  return request({
    url: '/system/api/v1/location/listTreeByParentCode',
    method: 'get',
    params: {
      parentCode: '',
      // 层级
      deep
    }
  })
}

// 创建导出任务
export function createDownLoad(
  className,
  fileName,
  groupName,
  menuName,
  searchConditionList = []
) {
  let obj = typeof className === 'object' ? className : {
    className,
    fileName,
    groupName,
    menuName,
    searchConditionList
  }
  return request({
    url: '/system/api/v1/admin/export/export',
    method: 'post',
    data: obj
  })
}

// 获取下载中列表
export function getDownLoadList(current, size) {
  return request({
    url: '/system/api/v1/admin/export/page',
    method: 'post',
    data: {
      current,
      size
    }
  })
}

// 下载文件
export function downLoadFile(id) {
  return request({
    url: '/system/api/v1/admin/export/downloadFile',
    method: 'get',
    params: {
      taskId: id
    }
  })
}

// 下载任务执行结果文件
export function importDownLoadFile(excelCode, id) {
  return request({
    url: `/system/api/v1/excel/import/${excelCode}/result/${id}`,
    method: 'get',
    params: {
    }
  })
}

// 展示导入模板信息
export function importGetConfig(
  excelCode
) {
  return request({
    url: `/system/api/v1/excel/import/${excelCode}/config`,
    method: 'get',
    params: {
    }
  })
}

// 导入任务分页
export function importPage(
  current,
  size,
  excelCode
) {
  return request({
    url: `/system/api/v1/excel/import/${excelCode}/page`,
    method: 'post',
    data: {
      current,
      size
    }
  })
}

// 获取字典数据
export function getDictList() {
  return request({
    url: '/system/api/v1/system/dict/all',
    method: 'get',
    params: {
    }
  })
}

// 刷新登录code验证码
export function refreshVcodeUrl() {
  const key = getRandomCode()
  const vCodeUrl = '/system/api/v1/captcha/image?serialNo=' + key
  return {
    url: process.env.VUE_APP_ADMIN_URL + vCodeUrl,
    code: key
  }
}

// 获取登录短信验证码
export function getLoginSmsCode(mobile) {
  return request({
    url: '/system/api/v1/auth/getLoginVerifyCode',
    method: 'post',
    data: {
      mobile
    }
  })
}

// 获取oss上传key
export function getOssKey(type = '', callbackUrl) {
  return request({
    url: `/system/api/v1/file/policy/${type}`,
    method: 'post',
    data: {
      callbackUrl: callbackUrl || ''
    }
  })
}
