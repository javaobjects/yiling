import store from '@/subject/pop/store'
import { getProtocolUrl } from '@/common/utils';
import { getCookieByKey, keys } from '@/subject/pop/utils/auth';
import router, { resetRouter } from '@/subject/pop/router'
export * from '@/common/utils/index'

// 获取接口字典内对应key的数组
export function getDict(key) {
  if (key) {
    let data = store.state.app.dict
    let sign = key + ''
    if (data && data.length) {
      let myDict = data.find(item => item.name == sign)
      if (myDict) {
        return myDict.dataList.map(itx => {
          itx.value = parseFloat(itx.value) || itx.value
          return itx
        })
      }
    }
  }
  return []
}

// 跳转登录页面
export function goLogin() {
  const userPlatform = getCookieByKey(keys.loginPlatformKey)
  if (userPlatform && userPlatform === 'b2b') {
    window.location.href = `${process.env.VUE_APP_H5_COMMON_URL}/b2b_login`
  } else {
    const popUrl = getProtocolUrl(window.location.hostname)
    window.location.href = getProtocolUrl(`${process.env.VUE_APP_POP_FRONT_DOMAIN}/login?redirect_url=${encodeURIComponent(popUrl)}`)
  }
}

/*
跳转其他tab页面
appId：
{"name":"企业数据管理","appId":5},{"name":"POP管理","appId":2},{"name":"B2B管理","appId":3},{"name":"销售助手","appId":6}]
routePath：为空则跳列表下第一个页面；有值的话正常path路径即可，比如：/products/products_index
*/
export const handleSelect = async (appId, routePath = '') => {
  routePath = typeof routePath === 'string' ? routePath : ''
  resetRouter()
  await store.dispatch('permission/setRoutes')
  await store.dispatch('app/togglePlatform', appId)
  let accessRoutes = await store.dispatch('permission/initRoutes', appId)
  router.addRoutes(accessRoutes)
  let routeStr = routePath
  if (!routeStr && accessRoutes.length > 1) {
    for (let i= 0; i< accessRoutes[1].children.length; i++) {
      const item = accessRoutes[1].children[i]
      if (!item.hidden) {
        routeStr = accessRoutes[1].path + '/' + item.path
        break
      }
    }
  }
  router.push(routeStr || '/')
}
