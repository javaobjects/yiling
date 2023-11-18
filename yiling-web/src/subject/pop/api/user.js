import request from '@/subject/pop/utils/request';
import store from '@/subject/pop/store';

// 账号登录
export function login(mobile, password, validateCode, serialNo) {
  return request({
    url: '/user/api/v1/auth/loginPassword',
    method: 'post',
    data: {
      mobile,
      password,
      validateCode,
      serialNo
    },
    common: 'USER',
    noErr: true
  });
}

// 退出
export function logout() {
  return request({
    url: '/user/api/v1/auth/logout',
    method: 'get',
    data: {},
    common: 'USER'
  });
}

// 短信登录
export function smsLogin(mobile, verifyCode) {
  return request({
    url: '/user/api/v1/auth/loginSms',
    method: 'post',
    data: {
      mobile,
      verifyCode
    },
    common: 'USER',
    noErr: true
  });
}

// 获取顶部菜单
export function getUserNavRouter(token) {
  if (store.getters.token) {
    return request({
      url: '/user/api/v1/menu/queryTopMenus',
      method: 'get',
      params: {},
      token: token || ''
    });
  }
}

// 获取侧边菜单
export function getUserSideRouter(appId) {
  if (store.getters.token) {
    return request({
      url: '/user/api/v1/menu/queryLeftMenus',
      method: 'get',
      params: {
        appId: parseInt(appId)
      },
      token: store.getters.token
    });
  }
}

// 获取验证码
export function getSmsCode(mobile) {
  return request({
    url: '/user/api/v1/auth/getLoginVerifyCode',
    method: 'post',
    data: {
      mobile
    },
    common: 'USER'
  });
}

// 获取重置密码验证码
export function getResetSmsCode(mobile) {
  return request({
    url: '/user/api/v1/auth/getResetPasswordVerifyCode',
    method: 'post',
    data: {
      mobile
    },
    common: 'USER'
  });
}

// 校验重置密码验证码
export function testSmsCode(mobile, verifyCode) {
  return request({
    url: '/user/api/v1/auth/checkResetPasswordVerifyCode',
    method: 'post',
    data: {
      mobile,
      verifyCode
    },
    common: 'USER',
    token: ''
  });
}

// 重置密码
export function resetPass(mobile, password, verifyCode) {
  return request({
    url: '/user/api/v1/auth/resetPassword',
    method: 'post',
    data: {
      mobile,
      password,
      verifyCode
    },
    common: 'USER'
  });
}
// 获取我的公司列表
export function getMyCompanyList(token) {
  return request({
    url: '/user/api/v1/user/getMyEnterpriseList',
    method: 'get',
    params: {},
    common: 'USER',
    token: token || ''
  });
}

// 切换我的公司
export function changeMyCompany(eid, token) {
  return request({
    url: '/user/api/v1/user/toggleEnterprise',
    method: 'get',
    params: {
      eid
    },
    common: 'USER',
    token: token || ''
  });
}

//  切换手机号  获取原手机号短信验证码
export function getOldPhoneVCode() {
  return request({
    url: '/user/api/v1/user/getOriginalMobileNumberVerifyCode',
    method: 'get',
    params: {},
    common: 'USER'
  });
}
//  切换手机号 校验原手机号短信验证码
export function checkOldPhoneVCode(verifyCode) {
  return request({
    url: '/user/api/v1/user/checkOriginalMobileNumberVerifyCode',
    method: 'get',
    params: {
      verifyCode
    },
    common: 'USER'
  });
}
//  切换手机号 获取更换绑定的新手机号短信验证码
export function getNewPhoneVCode(mobile, verifyCode) {
  return request({
    url: '/user/api/v1/user/getNewMobileNumberVerifyCode',
    method: 'post',
    data: {
      mobile,
      verifyCode
    },
    common: 'USER'
  });
}
//  切换手机号 获取更换绑定的新手机号短信验证码
export function checkNewPhoneVCode(mobile, originalVerifyCode, verifyCode) {
  return request({
    url: '/user/api/v1/user/checkNewMobileNumberVerifyCode',
    method: 'post',
    data: {
      mobile,
      originalVerifyCode,
      verifyCode
    },
    common: 'USER'
  });
}
