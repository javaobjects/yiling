import {MessageBox, Message, Notification, Loading} from 'element-ui'

export const log = (...parmas) => {
  if (process.env.NODE_ENV !== 'production') {
    console.log(...parmas)
  }
}

export const success = (title, callback) => {
  Message({
    message: title || '',
    type: 'success',
    showClose: true,
    duration: 3 * 1000,
    onClose: () => {
      if (callback) callback()
    }
  })
}

export const message = (title, callback) => {
  Message({
    message: title || '',
    type: 'info',
    showClose: true,
    duration: 3 * 1000,
    onClose: () => {
      if (callback) callback()
    }
  })
}

export const warn = (title, callback) => {
  Message({
    message: title || '',
    type: 'warning',
    showClose: true,
    duration: 3 * 1000,
    onClose: () => {
      if (callback) callback()
    }
  })
}

export const error = (title, callback) => {
  Message({
    message: title || '',
    type: 'error',
    showClose: true,
    duration: 3 * 1000,
    onClose: () => {
      if (callback) callback()
    }
  })
}

export const n_success = (title, callback, onclick) => {
  Notification({
    title: '提示',
    message: title || '',
    type: 'success',
    showClose: true,
    duration: 5 * 1000,
    onClose: () => {
      if (callback) callback()
    },
    onClick: () => {
      if (onclick) onclick()
    }
  })
}

export const n_message = (message, title, callback) => {
  Notification({
    title: title || '提示',
    message: message || '',
    type: 'info',
    showClose: true,
    duration: 5 * 1000,
    onClose: () => {
      if (callback) callback()
    },
    onClick: () => {
      if (onclick) onclick()
    }
  })
}

export const n_warn = (title, callback) => {
  Notification({
    title: '提示',
    message: title || '',
    type: 'warning',
    showClose: true,
    duration: 5 * 1000,
    onClose: () => {
      if (callback) callback()
    },
    onClick: () => {
      if (onclick) onclick()
    }
  })
}

export const n_error = (title, callback) => {
  Notification({
    title: '提示',
    message: title || '',
    type: 'error',
    showClose: true,
    duration: 5 * 1000,
    onClose: () => {
      if (callback) callback()
    },
    onClick: () => {
      if (onclick) onclick()
    }
  })
}

//模态弹框
export const alert = (msg, callback, options = {}) => {
  MessageBox.alert(
    msg || '',
    '提示',
    {
      callback: callback,
      ...options
    }
  )
}

//模态弹框 warn
export const alertWarn = (msg, callback) => {
  MessageBox.alert(
    msg || '',
    '提示',
    {
      type: 'warning',
      callback: callback
    }
  )
}

//模态弹框 info
export const alertInfo= (msg, callback) => {
  MessageBox.alert(
    msg || '',
    '提示',
    {
      type: 'info',
      callback: callback
    }
  )
}

//模态弹框 success
export const alertSuccess= (msg, callback) => {
  MessageBox.alert(
    msg || '',
    '提示',
    {
      type: 'success',
      callback: callback
    }
  )
}

//模态弹框 error
export const alertError= (msg, callback) => {
  MessageBox.alert(
    msg || '',
    '提示',
    {
      type: 'error',
      callback: callback
    }
  )
}

export const confirm = (msg, callback, confirmText, cancelText, options = {}, title) => {
  MessageBox.confirm(
    msg || '',
    title || '提示',
    {
      confirmButtonText: confirmText || '确定',
      cancelButtonText: cancelText || '取消',
      callback: (r) => {
        callback(r === 'confirm')
      },
      ...options
    }
  )
}

let loading = null
let timeOut = null

export const hideLoad = () => {
  if (loading) {
    loading.close()
    loading = null
    clearTimeout(timeOut)
  }
}

export const showLoad = (msg, target) => {
  clearTimeout(timeOut)
  if (loading === null) {
    loading = Loading.service({
      text: msg || '努力加载中...',
      spinner: '',
      background: 'rgba(255, 255, 255, 0.5)',
      target: document.querySelector(target || '.app-container')
    })
  }
  timeOut = setTimeout(() => {
    hideLoad()
  }, 10000)
}

export const showLongLoad = (msg, target) => {
  clearTimeout(timeOut)
  if (loading === null) {
    loading = Loading.service({
      text: msg || '努力加载中...',
      spinner: '',
      background: 'rgba(255, 255, 255, 0.5)',
      target: document.querySelector(target || '.app-container')
    })
  }
  timeOut = setTimeout(() => {
    hideLoad()
  }, 60000)
}

/**
 * @param {Function} func
 * @param {number} wait
 * @param {boolean} immediate
 * @return {*}
 */
export const debounce = (func, wait, immediate) => {
  let timeout, args, context, timestamp, result

  const later = function () {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp

    // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function (...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

// 转换时间 格式： yyyy-MM-dd h:m:s  yyyy-MM-dd hh:mm:ss
export const formatDate = (dates, formats) => {
  if (!dates || (typeof dates === 'number' && dates < 0)) {
    return '- -'
  }
  if (typeof dates === 'string' || typeof dates === 'number') {
    dates = new Date(dates)
  }
  let format = formats || 'yyyy-MM-dd hh:mm:ss'
  let date = {
    'M+': dates.getMonth() + 1,
    'd+': dates.getDate(),
    'h+': dates.getHours(),
    'm+': dates.getMinutes(),
    's+': dates.getSeconds(),
    'q+': Math.floor((dates.getMonth() + 3) / 3),
    'S+': dates.getMilliseconds()
  }
  if (/(y+)/i.test(format)) {
    format = format.replace(RegExp.$1, (dates.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (let k in date) {
    if (new RegExp('(' + k + ')').test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1
        ? date[k] : ('00' + date[k]).substr(('' + date[k]).length))
    }
  }
  return format
}

/**
 * Parse the time to string
 * @param {(Object|string|number)} time
 * @param {string} cFormat
 * @returns {string | null}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if ((typeof time === 'string')) {
      if ((/^[0-9]+$/.test(time))) {
        // support '1548221490638'
        time = parseInt(time)
      } else {
        // support safari
        // https://stackoverflow.com/questions/4310953/invalid-date-in-safari
        time = time.replace(new RegExp(/-/gm), '/')
      }
    }

    if ((typeof time === 'number') && (time.toString().length === 10)) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{([ymdhisa])+}/g, (result, key) => {
    const value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    return value.toString().padStart(2, '0')
  })
  return time_str
}

/**
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

/**
 * @param {string} url
 * @returns {Object}
 */
export function param2Obj(url) {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj = {}
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}

// 转换对象为get参数
export function paramObj2(params = {}) {
  let paramsStr = ''
  if (Object.keys(params).length === 0) {
    return paramsStr
  }
  for (var k in params) {
    paramsStr += k + '=' + params[k] + '&'
  }
  paramsStr = '?'+ paramsStr.substr(0, paramsStr.length - 1)
  return paramsStr
}


// 下载文件
export function downloadByUrl(url, name) {
  if (url) {
    let a = document.createElement('a')
    a.href = url
    if (name) {
      a.download = name
    }
    a.click()
  }
}

// 生成随机字符串
export function getRandomCode(length = 8) {
  if (length > 0) {
    let data = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];
    let nums = ''
    for (let i = 0; i < length; i++) {
      let r = parseInt(Math.random() * 61)
      nums += data[r]
    }
    return nums
  } else {
    return ''
  }
}

// 打开pdf
export function openPdf(url = '') {
  if (url) {
    const pdfUrl = `${process.env.VUE_APP_H5_URL}/pdfviewer/web/viewer.html?file=${url}`
    let el = document.createElement('a')
    document.body.appendChild(el)
    el.href = pdfUrl
    el.target = '_blank'
    el.click()
  }
}

// 获取添加协议头的地址
export const getProtocolUrl = (url) => {
  if (url) {
    return `${window.location.protocol}//${url}`
  }
  return ''
}

// 获取pop前台链接加跳转
export function getPopDomain(url = '') {
  return process.env.VUE_APP_POP_FRONT_DOMAIN + url
}

// 跳转前台链接
export function goPackage(type, query, newPage = true) {
  let url = process.env.VUE_APP_POP_FRONT_DOMAIN
  let route = ''
  switch (type) {
    case 1:
      // 跳转进货单
      route = '/cartProducts'
      break
    case 2:
      // 跳转收银台
      route = '/pay'
      break
    case 3:
      // 跳转提交订单
      route = '/order'
      break
    case 99:
      // 跳转首页
      route = ''
      break
  }
  let goUrl = getProtocolUrl(url + route + paramObj2(query))
  if (newPage) {
    let el = document.createElement('a')
    document.body.appendChild(el)
    el.href = goUrl
    el.target = '_blank'
    el.click()
  } else {
    window.location.href = goUrl
  }
  return goUrl
}

export function goThreePackage(url) {
  // window.open(url,"_blank");
  var a = document.createElement("a");
  a.setAttribute("href", url);
  a.setAttribute("target", "_blank");
  a.click();
}

// 克隆对象，深拷贝
export function clone(obj) {
  let o
  switch (typeof obj) {
    case 'undefined':
      break;
    case 'string':
      o = obj + ''
      break
    case 'number':
      o = obj - 0
      break
    case 'boolean':
      o = obj
      break
    case 'object':
      if (obj === null) {
        o = null
      } else {
        if (obj instanceof Array) {
          o = [];
          for (let i = 0, len = obj.length; i < len; i++) {
            o.push(clone(obj[i]))
          }
        } else {
          o = {};
          for (let k in obj) {
            o[k] = clone(obj[k])
          }
        }
      }
      break;
    default:
      o = obj
      break
  }
  return o
}

// px转vw
export function pxToVw(px, unit = 'vw') {
  if (!px) {
    return
  }
  let value = (px/1440)*100
  return value.toFixed(4) + unit
}

// 加
export function add(arg1, arg2) {
  let r1, r2, m;
  try {
    r1 = arg1.toString().split(".")[1].length
  } catch(e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split(".")[1].length
  } catch(e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2));
  return (arg1 * m + arg2 * m) / m;
}

// 减
export function sub(arg1, arg2) {
  return add(arg1, -arg2);
}

// 乘
export function mul(arg1, arg2) {
  let m = 0;
  let s1 = arg1.toString();
  let s2 = arg2.toString();
  try {
    m += s1.split(".")[1].length
  } catch(e) {}
  try {
    m += s2.split(".")[1].length
  } catch(e) {}
  return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
}

// 除
export function div(arg1, arg2) {
  let t1 = 0,
    t2 = 0,
    r1, r2;
  try {
    t1 = arg1.toString().split(".")[1].length
  } catch(e) {}
  try {
    t2 = arg2.toString().split(".")[1].length
  } catch(e) {}

  r1 = Number(arg1.toString().replace(".", ""))
  r2 = Number(arg2.toString().replace(".", ""))
  return(r1 / r2) * Math.pow(10, t2 - t1);
}

// 舍尾法截取数字（非四舍五入）
export function interceptNumber(number, count) {
  if(number != null && count > 0) {
    let str = "" + number;
    let regValue = str.match(new RegExp("\\d+\\.\\d{" + count + "}", "gm"));
    if(regValue != null) {
      let value = parseFloat(regValue);
      if(value != null) {
        return value;
      }
    }

  }
  return number;
}

/**
 * 计算 - 加
 * @param {Number} arg1 加数
 * @param {Array} arg2  [被加数1, 被加数2, 被加数3, ...]
 * @param {Number} fixed 保留小数位数
 * @return {String} 计算结果
 */
export const numberAdd = (arg1, arg2 = [], fixed = -1) => {
  let x = new BigNumber(arg1)
  arg2.map(n => {
    x = x.plus(n)
  })
  if (fixed > -1) {
    return x.toFixed(fixed)
  }
  return x.toString()
}

/**
 * 计算 - 减
 * @param {Number} arg1 减数
 * @param {Array} arg2  [被减数1, 被减数2, 被减数3, ...]
 * @param {Number} fixed 保留小数位数
 * @return {String} 计算结果
 */
export const numberSub = (arg1, arg2 = [], fixed = -1) => {
  let x = new BigNumber(arg1)
  arg2.map(n => {
    x = x.minus(n)
  })
  if (fixed > -1) {
    return x.toFixed(fixed)
  }
  return x.toString()
}

/**
 * 计算 - 乘
 * @param {Number} arg1 乘数
 * @param {Array} arg2  [被乘数1, 被乘数2, 被乘数3, ...]
 * @param {Number} fixed 保留小数位数
 * @return {String} 计算结果
 */
export const numberMul = (arg1, arg2 = [], fixed = -1) => {
  let x = new BigNumber(arg1)
  arg2.map(n => {
    x = x.times(n)
  })
  if (fixed > -1) {
    return x.toFixed(fixed)
  }
  return x.toString()
}

/**
 * 计算 - 除
 * @param {Number} arg1 除数
 * @param {Array} arg2  [被除数1, 被除数2, 被除数3, ...]
 * @param {Number} fixed 保留小数位数
 * @return {String} 计算结果
 */
export const numberDiv = (arg1, arg2 = [], fixed = -1) => {
  let x = new BigNumber(arg1)
  arg2.map(n => {
    x = x.div(n)
  })
  if (fixed > -1) {
    return x.toFixed(fixed)
  }
  return x.toString()
}

/**
 * 计算 - 取模、取余
 * @param {Number} arg1 数
 * @param {Array} arg2  [模1, 模2, 模3, ...]
 * @param {Number} fixed 保留小数位数
 * @return {String} 计算结果
 */
export const numberMod = (arg1, arg2 = [], fixed = -1) => {
  let x = new BigNumber(arg1)
  arg2.map(n => {
    x = x.mod(n)
  })
  if (fixed > -1) {
    return x.toFixed(fixed)
  }
  return x.toString()
}

// 获取当前年份的前后num年
export function getBeforeYearsList(num) {
  let cur = new Date().getFullYear();
  let cur1;
  let before = [];
  for (let i = 0;i < num;i++) {
    cur1 =cur- (1 + i);
    before.push(cur1);//在开头添加
  }
  return [ cur, ...before];
}

/**
* oninput 限制输入框小数点位数，多出的过滤掉
* @param  Number     {num}
* @param  Number     {limit}
 */
export function onInputLimit(num, limit, maxLength = 9) {
  var str = num
  var len1 = str.substr(0, 1)
  var len2 = str.substr(1, 1)
  //如果第一位是0，第二位不是点，就用数字把点替换掉
  if (str.length > 1 && len1 == 0 && len2 != ".") {
    str = str.substr(1, 1)
  }
  //第一位不能是.
  if (len1 == ".") {
    str = ""
  }
  //限制只能输入一个小数点
  if (str.indexOf(".") != -1) {
    var str_ = str.substr(str.indexOf(".") + 1)
    if (str_.indexOf(".") != -1) {
      str = str.substr(0, str.indexOf(".") + str_.indexOf(".") + 1)
    }
  }
  //正则替换
  str = str.replace(/[^\d^\.]+/g, '') // 保留数字和小数点
  if (limit == 0) {
    str = str.replace(/^\D*([0-9]\d*\.?\d{0,0})?.*$/,'$1') // 小数点后只能输 0 位
    if (str.indexOf(".") != -1) {
      str = str.replace('.', '')
    }
  }
  if (limit > 0) {
    let reg = new RegExp('^\\D*([0-9]\\d*\.?\\d{0,' + limit + '})?.*$')
    str = str.replace(reg,'$1') // 小数点后只能输 limit 位
  } 
  // 判断整数位最多为9位
  if (str.toString().indexOf('.') > 0 && Number(str.toString().split('.')[0].length) > maxLength) {
    str = str.toString().substring(0, maxLength) + '.' + str.toString().split('.')[1]
  } else if (str.toString().indexOf('.') < 0 && Number(str.toString().split('.')[0].length) > maxLength) {
    str = str.toString().substring(0, maxLength)
  }
  return str
}

// 公共方法部分
/* 校验输入正负数， 保留2位小数 传来的需要是string类型*/
export function plusOrMinus(values) {
  let newValue
  if (!(/[^0-9.-]/g.test(values))) {
    newValue = values.replace(/[^\-\d.]/g, '').replace(/\b(0+){2,}/g, '0').replace(/\-{2,}/g, '-').replace(/^\./g, '').replace(/\.{2,}/g, '.').replace('.', '$#$').replace(/\./g, '').replace('$#$', '.')
    if (newValue.toString().indexOf('.') > 0 && Number(newValue.toString().split('.')[1].length) > 2) {
      newValue = parseInt(parseFloat(newValue) * 100) / 100
    }
    if ((newValue.toString().split('-').length - 1) > 1) {
      newValue = parseFloat(newValue) || ''
    }
    if ((newValue.toString().split('-').length) > 1 && newValue.toString().split('-')[0].length > 0) {
      newValue = parseFloat(newValue) || ''
    }
    if (newValue.toString().length > 1 && (newValue.toString().charAt(0) === '0' || (newValue.toString().length > 2 && newValue.toString().charAt(0) === '-' && newValue.toString().charAt(1) === '0' && newValue.toString().charAt(2) !== '.')) && newValue.toString().indexOf('.') < 1) {
      newValue = parseFloat(newValue) || ''
    }
    // 判断整数位最多为9位
    if (newValue.toString().indexOf('.') > 0 && Number(newValue.toString().split('.')[0].length) > 9) {
      newValue = newValue.toString().substring(0, 9) + '.' + newValue.toString().split('.')[1]
    } else if (newValue.toString().indexOf('.') < 0 && Number(newValue.toString().split('.')[0].length) > 9) {
      newValue = newValue.toString().substring(0, 9)
    }
  } else {
    newValue = values.replace(/[^0-9.-]/g, '')
  }
  return newValue
}

/**
* isRepeat 判断数组中是否有重复元素
* @param  Number     {arr}
 */
export function isRepeat(arr) {
  let hash = {};
  for (var i in arr) {
      if (hash[arr[i]]){
          return true;
      }
      hash[arr[i]] = true;
  }
  return false;
}

/**
 * 判断浏览器
 */
export function browser() {
  var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
  var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
  var isIE = (!!window.ActiveXObject || "ActiveXObject" in window); //判断是否IE浏览器
// var isIE = userAgent.indexOf("compatible") > -1
// && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器
  var isEdge = userAgent.indexOf("Edg") > -1; //判断是否IE的Edge浏览器
  var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
  var isSafari = userAgent.indexOf("Safari") > -1
    && userAgent.indexOf("Chrome") == -1; //判断是否Safari浏览器
  var isChrome = userAgent.indexOf("Chrome") > -1
    && userAgent.indexOf("Safari") > -1; //判断Chrome浏览器

  if (isIE) {
    var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
    reIE.test(userAgent);
    var fIEVersion = parseFloat(RegExp["$1"]);
    if (fIEVersion == 7) {
      return "IE7";
    } else if (fIEVersion == 8) {
      return "IE8";
    } else if (fIEVersion == 9) {
      return "IE9";
    } else if (fIEVersion == 10) {
      return "IE10";
    } else if (userAgent.indexOf("rv:11.0") > -1) {
      return "IE11";
    } else {
      return "0";
    }//IE版本过低
    return "IE";
  }
  if (isOpera)
    return "Opera";
  if (isEdge)
    return "Edge";
  if (isFF)
    return "FF";
  if (isSafari)
    return "Safari";
  if (isChrome)
    return "Chrome";
}

// 获取uuid
export const getUUID = () => {
  let s = []
  let hexDigits = "0123456789abcdef"
  for (let i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
  }
  s[14] = "4"
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1)
  s[8] = s[13] = s[18] = s[23] = "-"
  let uuid = s.join("")
  return uuid
}

const sessionMaxExpire = 12 * 60 * 1000 //12分钟
const isExpireTime = function(lastTime, maxExpire) {
  const current = new Date().getTime()
  if (current - lastTime > maxExpire) {
    return true
  } else {
    return false
  }
}
// 设置带缓存sessionStorage
export const setSessionCache = function(key, value) {
  if(key && value) {
    sessionStorage.setItem(key, JSON.stringify({
      data: value,
      time: new Date().getTime()
    }))
  }
}
// 获取缓存sessionStorage
export const getSessionCache = function(key) {
  if(key) {
    const cacheData = JSON.parse(sessionStorage.getItem(key));
    if (cacheData) {
      const lastTime = cacheData.time
      if(lastTime && !isExpireTime(lastTime, sessionMaxExpire)) {
        return cacheData.data
      }
    }
  }
  return null;
}

/**
* isSortArray 根据数组需要冒泡的次数判断数组为顺序,逆序还是无序
* @param  Number     {array}
 */
export function isSortArray(array) {
  // 冒泡的次数
  let count = 0;
  // 上一次交换的"气泡"
  let pop = array[0];
  // 利用单次冒泡检验数组需要冒泡的次数
  for (let j = 0; j < array.length - 1; j++) {
    if (array[j] > array[j + 1]) {
      // 判断此次需要向上冒的气泡是否和上次的相同,不同为乱序
      if (pop != array[j]) {
        // 若气泡发生了变化,证明是无序的,停止遍历,返回 0
        return 0;
      }

      let temp = array[j];
      array[j] = array[j + 1];
      array[j + 1] = temp;
      // 两相邻元素交换一次值,count计数一次
      count++;
      pop = array[j + 1];
      //气泡向右移动，可能是逆序，对于逆序序列，还需保证气泡左侧所有的元素大于右侧
      //所以当左侧最小的元素小于右侧元素时，为无序序列
      if (j + 2 < array.length ){
        if (array[j] < array[j+2]){
        return 0;
        }
      }
    }
  }
  // 正序返回 1, 倒序返回 -1
  if (count == 0) {
      return 1;
  } else {
      return - 1;
  }
}
