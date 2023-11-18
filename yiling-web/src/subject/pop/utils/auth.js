import Cookies from 'js-cookie'

// 转换key按环境生成
const transformKey = (key) => {
  if (process.env.NODE_ENV !== 'production') {
    return `${process.env.NODE_ENV}_${key}`
  }
  return key
}

const tokenKey = transformKey('YL_MANAGE_LOGIN_TOKEN')
const userInfoKey = transformKey('USER_INFO')
const platformKey = transformKey('MANAGER_PLATFORM')
const companyKey = transformKey('USER_COMPANY')
const loginPlatformKey = transformKey('USER_LOGIN_PLATFORM')

// 失效时间3天
const day = 3
const time = new Date(new Date().getTime() + day * 60 * 60 * 1000 * 24)

// 获取前台二级域名
function getPopDomain() {
  let domain = process.env.VUE_APP_POP_FRONT_DOMAIN
  return domain.slice(domain.indexOf('.'))
}

export function getToken() {
  return Cookies.get(tokenKey)
}

export function setToken(token) {
  Cookies.set(tokenKey, token, {
    domain: process.env.NODE_ENV !== 'dev' ? getPopDomain() : undefined,
    expires: time
  })
}

export function removeToken() {
  if (process.env.NODE_ENV !== 'dev') {
    Cookies.set(tokenKey, null, {
      domain: getPopDomain(),
      expires: 0
    })
  }
  Cookies.remove(tokenKey)
}

export function getCookieByKey(key) {
  let value = Cookies.get(key)
  if (typeof value === 'string' && (value.indexOf('{') > -1 || value.indexOf('[') > -1)) {
    value = JSON.parse(value)
  }
  return value
}

export function setCookieByKey(key, value) {
  if (value) {
    if (typeof value === 'object') {
      value = JSON.stringify(value)
    }
    Cookies.set(key, value, {
      domain: process.env.NODE_ENV !== 'dev' ? getPopDomain() : undefined,
      expires: time
    })
  }
}

export function removeCookieByKey(key) {
  if (process.env.NODE_ENV !== 'dev') {
    Cookies.set(key, null, {
      domain: getPopDomain(),
      expires: 0
    })
  }
  Cookies.remove(key)
}

// 获取当前用户信息
export function getCurrentUser() {
  return getCookieByKey(userInfoKey)
}

// 获取当前顶部菜单id
export function getPlatform() {
  return getCookieByKey(platformKey) || ''
}

// 设置当前顶部菜单id
export function setPlatform(value) {
  if (typeof value === 'undefined') {
    return
  }
  setCookieByKey(platformKey, value)
}

// 删除当前顶部菜单id
export function removePlatform() {
  removeCookieByKey(platformKey)
}

export const keys = {
  tokenKey,
  userInfoKey,
  platformKey,
  companyKey,
  loginPlatformKey
}
