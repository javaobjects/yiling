import request from '@/subject/pop/utils/request'
import { getRandomCode } from '@/subject/pop/utils/index'

// 获取地区级联选择器
export function getLocation(parentCode) {
  return request({
    url: '/user/api/v1/location/listByParentCode',
    method: 'get',
    params: {
      parentCode
    },
    common: 'USER'
  })
}

// 获取区域树结构列表
export function getLocationTree(deep) {
  return request({
    url: '/user/api/v1/location/listTreeByParentCode',
    method: 'get',
    params: {
      parentCode: '',
      // 层级
      deep
    }
  })
}

// 创建导出任务
export function createDownLoad(options) {
  return request({
    url: '/admin/dataCenter/api/v1/data/export/export',
    method: 'post',
    data: options
  })
}

// 获取下载中列表
export function getDownLoadList(current, size) {
  return request({
    url: '/admin/dataCenter/api/v1/data/export/page',
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
    url: '/admin/dataCenter/api/v1/data/export/downloadFile',
    method: 'get',
    params: {
      taskId: id
    }
  })
}

// 下载任务执行结果文件
export function importDownLoadFile(excelCode, id) {
  return request({
    url: `/admin/dataCenter/api/v1/excel/import/${excelCode}/result/${id}`,
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
    url: `/admin/dataCenter/api/v1/excel/import/${excelCode}/config`,
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
    url: `/admin/dataCenter/api/v1/excel/import/${excelCode}/page`,
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
    url: '/user/api/v1/dict/all',
    method: 'get',
    params: {
    },
    common: 'USER'
  })
}

// 刷新登录code验证码
export function refreshVcodeUrl() {
  const key = getRandomCode()
  const vCodeUrl = '/user/api/v1/captcha/image?serialNo=' + key
  return {
    url: process.env.VUE_APP_REQ_URL + vCodeUrl,
    code: key
  }
}

// 获取登录短信验证码
export function getLoginSmsCode(mobile) {
  return request({
    url: '/user/api/v1/auth/getLoginVerifyCode',
    method: 'post',
    data: {
      mobile
    },
    common: 'USER'
  })
}

// 获取oss上传key
export function getOssKey(type = '') {
  return request({
    url: `/admin/dataCenter/api/v1/file/policy/${type}`,
    method: 'get',
    params: {
    }
  })
}
