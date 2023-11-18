
/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

// 校验用户手机号
export const validateTel = (value) => {
  const myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/
  if (!myreg.test(value)) {
    return false
  } else {
    return true
  }
}

// 校验是否为数字
export const validateIsNum = (nubmer) => {
  let reg = /(^[\-0-9][0-9]*(.[0-9]+)?)$/
  if (!reg.test(nubmer)) {
    return false
  }
  return true
}

// 校验小数位是否超过限制位数
export const validateNumFloatLength = (nubmer, fixed) => {
  if (nubmer === 0 || nubmer) {
    let newValue = nubmer.toString().split('.')[1]
    if (newValue && newValue.length > fixed) {
      return true
    }
  }
  return false
}

// 校验是否为正数
export const validateIsNumZ = (nubmer) => {
  if (nubmer == 0) {
    return false
  }
  let reg = /^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/
  if (!reg.test(nubmer)) {
    return false
  }
  return true
}

// 校验是否为正整数
export const validateIsNumZZ = (nubmer) => {
  let reg = /^[0-9]*[1-9][0-9]*$/
  if (!reg.test(nubmer)) {
    return false
  }
  return true
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}
