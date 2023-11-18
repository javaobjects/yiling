//公众号管理
import request from '@/subject/admin/utils/request'
//公众号欢迎语列表
export function pageList(
  //场景id
  sceneId,
  current,
	size
){
  return request({
    url: '/hmc/api/v1/gzhGreeting/pageList',
    method: 'post',
    data: {
      sceneId,
      current,
      size
    }
  })
}
//保存公众号欢迎语
export function saveGreetings(
  id,
  //场景id
  sceneId,
  //运营备注
  remark,
  //欢迎语
  draftVersion
){
  return request({
    url: '/hmc/api/v1/gzhGreeting/saveGreetings',
    method: 'post',
    data: {
      id,
      sceneId,
      remark,
      draftVersion
    }
  })
}
//公众号欢迎语详情
export function getDetailById(
  id
){
  return request({
    url: '/hmc/api/v1/gzhGreeting/getDetailById',
    method: 'post',
    data: {
      id
    }
  })
}
//发布公众号欢迎语
export function publishGreetings(
  id
){
  return request({
    url: '/hmc/api/v1/gzhGreeting/publishGreetings',
    method: 'post',
    data: {
      id
    }
  })
}
//公众号菜单
export function menuListInfo(
){
  return request({
    url: '/hmc/api/v1/gzhMenu/menuListInfo',
    method: 'post',
    data: {}
  })
}
//发布公众号菜单
export function publishMenu(
  //菜单数据
  button
){
  return request({
    url: '/hmc/api/v1/gzhMenu/publishMenu',
    method: 'post',
    data: {
      button
    }
  })
}