import {
  validateIsNum,
  validateIsNumZ,
  validateIsNumZZ,
  validateNumFloatLength
} from './validate'

const notEmpty = (value) => {
  return typeof value !== 'undefined' && value !== null && value !== ''
}

// 校验是否为数字
export const isNum = (rule, value, callback) => {
  if (rule.required || (notEmpty(value) && !rule.required)) {
    if (!notEmpty(value)) {
      callback(new Error('请输入数字'))
    } else if (!validateIsNum(value)) {
      callback(new Error('请输入正确的数字'))
    } else if (rule.fixed) {
      if (validateNumFloatLength(value, rule.fixed)) {
        callback(new Error(`仅支持输入${rule.fixed}位小数`))
      } else {
        callback()
      }
    } else {
      callback()
    }
  } else {
    callback()
  }
}

// 校验是否为正数
export const isNumZ = (rule, value, callback) => {
  if (rule.required || (notEmpty(value) && !rule.required)) {
    if (!notEmpty(value)) {
      callback(new Error('请输入数字'))
    } else if (!validateIsNumZ(value)) {
      callback(new Error('请输入正数'))
    } else if (rule.fixed) {
      if (validateNumFloatLength(value, rule.fixed)) {
        callback(new Error(`仅支持输入${rule.fixed}位小数`))
      } else {
        callback()
      }
    } else {
      callback()
    }
  } else {
    callback()
  }
}

// 校验是否为正整数
export const isNumZZ = (rule, value, callback) => {
  if (rule.required || (notEmpty(value) && !rule.required)) {
    if (!notEmpty(value)) {
      callback(new Error('请输入数字'))
    } else if (!validateIsNumZZ(value)) {
      callback(new Error('请输入正确的正整数'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}
