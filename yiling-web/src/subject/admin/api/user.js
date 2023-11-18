import request from '@/subject/admin/utils/request'

export function login(username, password, validateCode, serialNo) {
  return request({
    url: '/system/api/v1/auth/login',
    method: 'post',
    data: {
      username,
      password,
      validateCode,
      serialNo
    }
  })
}

// 退出
export function logout() {
  return request({
    url: '/system/api/v1/auth/logout',
    method: 'get'
  })
}
// 获取顶部菜单
export function getUserNavRouter() {
  return request({
    url: '/system/api/v1/menu/queryTopMenus',
    method: 'get',
    params: {
    }
  })
}

// 获取侧边菜单
export function getUserSideRouter(appId) {
  return request({
    url: '/system/api/v1/menu/queryLeftMenus',
    method: 'get',
    params: {
      parentId: parseInt(appId)
    }
  })
}

// 获取重置密码验证码
export function getResetSmsCode(mobile) {
  return request({
    url: '/system/api/v1/auth/getResetPasswordVerifyCode',
    method: 'post',
    data: {
      mobile
    }
  })
}

// 校验重置密码验证码
export function testSmsCode(mobile, verifyCode) {
  return request({
    url: '/system/api/v1/auth/checkResetPasswordVerifyCode',
    method: 'post',
    data: {
      mobile,
      verifyCode
    }
  })
}

// 重置密码
export function resetPass(mobile, password, verifyCode) {
  return request({
    url: '/system/api/v1/auth/resetPassword',
    method: 'post',
    data: {
      mobile,
      password,
      verifyCode
    }
  })
}

// 重置密码 原密码 新密码确认新密码
export function updatePassword(oldPassword, password) {
  return request({
    url: '/system/api/v1/user/updatePassword',
    method: 'post',
    data: {
      oldPassword,//原密码
      password//新密码
    }
  });
}
