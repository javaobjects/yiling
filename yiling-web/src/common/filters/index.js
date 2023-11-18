export { formatTime, formatDate, pxToVw } from '@/common/utils'

/**
 * 10000 => "10,000"
 * @param {number} num
 * @param {string} sign 金额单位
 * @param {bool} zero true:为0则展示- -
 */
export function toThousand(num, sign = '', zero = false) {
  if (typeof num === 'undefined' || num === null || (zero && num == 0)) {
    return '- -'
  }
  if (sign && sign.indexOf('-') > -1 && num == 0) {
    sign = sign.replace('-', '')
  }
  return sign + (+num || 0).toString().replace(/^-?\d+/g, m => m.replace(/(?=(?!\b)(\d{3})+$)/g, ','))
}

// 是或否
export function yes(e) {
  return parseInt(e) === 1 ? '是' : '否'
}

// 启用禁用
export function enable(e) {
  return parseInt(e) === 1 ? '启用' : '停用'
}

// 根据数组取键值的label
export function dictLabel(key, array) {
  let str = '- -'
  if (Array.isArray(array) && array.length) {
    let myDict = array.find(item => item.value == key)
    if (myDict) {
      str = myDict.label
    }
  }
  return str
}

// 根据数组取键值的value
export function dictValue(key, array) {
  let str = -1
  if (Array.isArray(array) && array.length) {
    let myDict = array.find(item => item.value == key)
    if (myDict) {
      str = parseInt(myDict.value || 0)
    }
  }
  return str
}
