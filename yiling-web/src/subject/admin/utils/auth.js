import Cookies from 'js-cookie'

const TokenKey = 'YL_MANAGE_YY_TOKEN'

//失效时间3天
const day = 3
const time = new Date(new Date().getTime() + day * 60 * 60 * 1000 * 24)

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token, {
    expires: time
  })
}

export function removeToken() {
  return Cookies.remove(TokenKey)
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
      expires: time
    })
  }
}

export function removeCookieByKey(key) {
  return Cookies.remove(key)
}

// 获取当前用户信息
export function getCurrentUser() {
  return getCookieByKey('YY_USER_INFO')
}

// 获取当前顶部菜单id
export function getPlatform() {
  return getCookieByKey('YY_MANAGER_PLATFORM') || ''
}

// 设置当前顶部菜单id
export function setPlatform(value) {
  if (typeof value === 'undefined') {
    return
  }
  setCookieByKey('YY_MANAGER_PLATFORM', value)
}

// 删除当前顶部菜单id
export function removePlatform() {
  removeCookieByKey('YY_MANAGER_PLATFORM')
}
