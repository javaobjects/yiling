import store from '@/subject/admin/store'
import router, { resetRouter } from '@/subject/admin/router'
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

/*
跳转其他tab页面
appId：
{"id":256,"menuName":"中台数据管理"},
{"id":111,"menuName":"POP管理"},
{"id":255,"menuName":"B2B管理"},
{"id":243,"menuName":"销售助手"},
{"id":557,"menuName":"ERP对接管理"},
{"id":143,"menuName":"系统管理"}
routePath：为空则跳列表下第一个页面
*/
export const handleSelect = async (platform, routePath = '') => {
  routePath = typeof routePath === 'string' ? routePath : ''
  resetRouter()
  store.dispatch('tagsView/delAllVisitedViews')
  await store.dispatch('permission/setRoutes')
  await store.dispatch('app/togglePlatform', platform)
  let accessRoutes = await store.dispatch('permission/initRoutes', platform)
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
