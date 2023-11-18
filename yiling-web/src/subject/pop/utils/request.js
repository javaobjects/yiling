import axios from 'axios'
import { getToken } from './auth'
import { error, log, hideLoad, alert, showLoad, goLogin } from '@/subject/pop/utils/index'
import store from '@/subject/pop/store'
import router from '../router'

const TIME_OUT = 15000

const service = axios.create({
  baseURL: process.env.VUE_APP_REQ_URL,
  // withCredentials: true,
  timeout: TIME_OUT
})

service.interceptors.request.use(
  config => {
    let token = store.getters.token || getToken()
    if (typeof config.token === 'string' && config.token) {
      config.headers['Authorization'] = `Bearer ${config.token}`
    } else {
      if (typeof token === 'string' && token) {
        config.headers['Authorization'] = `Bearer ${token}`
      }
    }
    if (Object.prototype.toString.call(config.data) === '[object FormData]') {
      config.headers['Content-Type'] = 'multipart/form-data'
      config.timeout = 60000
    } else {
      config.headers['Content-Type'] = 'application/json'
      config.timeout = config.timeout || TIME_OUT
    }
    log('请求参数：->' + config.url + '=========>：' + JSON.stringify(config.data || config.params))
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      log('返回错误：->' + JSON.stringify(res))
      hideLoad()
      // 拦截错误 (默认拦截，noErr设置为true则不报错)
      if (!response.config.noErr) {
        error(res.message)
      } else {
        // 错误自己处理
        return Promise.resolve(res)
      }
    } else {
      let data = res.data
      log('请求url：->' + response.config.url)
      log('返回参数：->' + JSON.stringify(data))
      return Promise.resolve(data)
    }
  },
  err => {
    log('error -> ' + err)
    hideLoad()
    let response = err.response
    if (response.status === 401) {
      alert('会话已过期，请重新登录', async () => {
        showLoad()
        await store.dispatch('user/resetToken')
        hideLoad()
        if (process.env.NODE_ENV === 'dev') {
          router.replace('/login')
        } else {
          goLogin()
        }
      })
      return
    } else if (response.status === 403) {
      router.replace('/403')
      return
    }
    if (response.data) error(response.data.message)
    // return Promise.reject(err)
  }
)

export default service
