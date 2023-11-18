import request from '@/subject/admin/utils/request'

// 用户分页列表
export function getSystemAdminList(
  current,
  size,
  // 角色名
  username,
  // 状态
  status,
  beginTime,
  endTime
) {
  return request({
    url: '/system/api/v1/system/admin/pageList',
    method: 'post',
    data: {
      current,
      size,
      username,
      status,
      beginTime,
      endTime
    }
  })
}

// 角色启用/停用
export function systemupdateStatus(
  userId,
  status
) {
  return request({
    url: '/system/api/v1/system/admin/updateStatus',
    method: 'post',
    data: {
      userId,
      status
    }
  })
}

// 保存用户信息
export function saveSystemAdmin(
  username,
  name,
  nickname,
  birthday,
  email,
  gender,
  status,
  roleIdList,
  password,
  id, 
  mobile
) {
  return request({
    url: '/system/api/v1/system/admin/save',
    method: 'post',
    data: {
      username,
      name,
      nickname,
      birthday,
      email,
      gender,
      status,
      roleIdList,
      password,
      id,
      mobile
    }
  })
}

// 运营后台所有角色列表
export function getAllRoleList(
) {
  return request({
    url: '/system/api/v1/role/list',
    method: 'get',
    params: {
    }
  })
}

// 获取用户信息
export function getSystemAdminDetail( id ) {
    return request({
      url: '/system/api/v1/system/admin/get',
      method: 'get',
      params: {
        id
      }
    })
  }

// 重置密码
export function resetPassword( mobile ) {
  return request({
    url: '/system/api/v1/user/resetPassword',
    method: 'get',
    params: {
      mobile
    }
  })
}

// 角色分页列表
export function getSystemRoleList(
  current,
  size,
  // 应用ID：1-运营后台 2-POP后台 3-商城后台 4-互联网医院后台
  appId,
  // 角色名
  name,
  status
) {
  return request({
    url: '/system/api/v1/role/pageList',
    method: 'post',
    data: {
      current,
      size,
      appId,
      name,
      status
    }
  })
}

// 创建角色
export function createRole(
  name,
  status,
  type,
  remark
) {
  return request({
    url: '/system/api/v1/role/create',
    method: 'post',
    data: {
      name,
      status,
      type,
      remark
    }
  })
}

// 修改角色
export function updateRole(
  id,
  name,
  status,
  type,
  remark,
  code
) {
  return request({
    url: '/system/api/v1/role/update',
    method: 'post',
    data: {
      id,
      name,
      status,
      type,
      remark,
      code
    }
  })
}

// 删除角色
export function deleteRole(
  roleIdList
) {
  return request({
    url: '/system/api/v1/role/batchDelete',
    method: 'post',
    data: {
      roleIdList
    }
  })
}

// 获取角色菜单详情
export function getRoleMenu( id ) {
  return request({
    url: '/system/api/v1/role/getRoleMenu',
    method: 'get',
    params: {
      id
    }
  })
}

// 创建角色关联菜单
export function createRoleMenu(
  roleId,
  menuIdList
) {
  return request({
    url: '/system/api/v1/role/createRoleMenu',
    method: 'post',
    data: {
      roleId,
      menuIdList
    }
  })
}

// 修改角色关联菜单
export function updateRoleMenu(
  roleId,
  menuIdList
) {
  return request({
    url: '/system/api/v1/role/updateRoleMenu',
    method: 'post',
    data: {
      roleId,
      menuIdList
    }
  })
}

// 获取菜单所属的应用列表
export function listMenuApps( ) {
  return request({
    url: '/system/api/v1/menu/listMenuApps',
    method: 'get',
    params: {
    }
  })
}

// 获取菜单管理列表
export function getMenusList( appId ) {
  return request({
    url: '/system/api/v1/menu/queryMenusList',
    method: 'get',
    params: {
      appId
    }
  })
}

// 获取菜单目录树
export function queryCatalogTree( appId ) {
  return request({
    url: '/system/api/v1/menu/queryCatalogTree',
    method: 'get',
    params: {
      appId
    }
  })
}

// 新增菜单
export function createMenu(
  appId,
  menuType,
  menuName,
  menuStatus,
  menuIdentification,
  menuUrl,
  sortNum,
  parentId,
  remark
) {
  return request({
    url: '/system/api/v1/menu/create',
    method: 'post',
    data: {
      appId,
      menuType,
      menuName,
      menuStatus,
      menuIdentification,
      menuUrl,
      sortNum,
      parentId,
      remark
    }
  })
}

// 删除菜单
export function deleteMenu(
  menuIdList
) {
  return request({
    url: '/system/api/v1/menu/batchDelete',
    method: 'post',
    data: {
      menuIdList
    }
  })
}

// 修改菜单
export function updateMenu(
  id,
  appId,
  menuType,
  menuName,
  menuStatus,
  menuIdentification,
  menuUrl,
  sortNum,
  parentId,
  remark
) {
  return request({
    url: '/system/api/v1/menu/update',
    method: 'post',
    data: {
      id,
      appId,
      menuType,
      menuName,
      menuStatus,
      menuIdentification,
      menuUrl,
      sortNum,
      parentId,
      remark
    }
  })
}
// 字典接口
// 获取字典类型数据
export function dicType(
  current, //第几页
	name, //字典名称/描述
	size //每页记录数
){
  return request({
    url: '/system/api/v1/system/dict/get/type',
    method: 'post',
    data: {
      current,
      name,
      size
    }
  })
}
// 获取字典内容数据
export function dicGetData(
  typeId //id
){
  return request({
    url: '/system/api/v1/system/dict/get/data',
    method: 'get',
    params: {
      typeId
    }
  })
}
// 停用字典类型数据
export function stopType(
  id //id
){
  return request({
    url: '/system/api/v1/system/dict/stop/type',
    method: 'get',
    params: {
      id
    }
  })
}
// 启用字典类型数据
export function enabledType(
  id //id
){
  return request({
    url: '/system/api/v1/system/dict/enabled/type',
    method: 'get',
    params: {
      id
    }
  })
}
// 停用字典内容数据
export function stopData(
  id //id
){
  return request({
    url: '/system/api/v1/system/dict/stop/data',
    method: 'get',
    params: {
      id
    }
  })
}
// 启用字典内容数据
export function enabledData(
  id //id
){
  return request({
    url: '/system/api/v1/system/dict/enabled/data',
    method: 'get',
    params: {
      id
    }
  })
}
// 新增字典类型数据
export function saveType(
  description, //字典描述
  name //字典名称
){
  return request({
    url: '/system/api/v1/system/dict/save/type',
    method: 'post',
    data: {
      description, name
    }
  })
}
// 新增字典内容数据
export function saveData(
  description, //字典描述
  label, //字典标签
  typeId, //	字典类型ID
  value, //字典键值
  sort //字典排序
){
  return request({
    url: '/system/api/v1/system/dict/save/data',
    method: 'post',
    data: {
      description, label, typeId, value, sort
    }
  })
}
// 删除 右侧字典数据
export function dataDelete(
  idList //id
){
  return request({
    url: '/system/api/v1/system/dict/data/delete',
    method: 'get',
    params: {
      idList
    }
  })
}
// 左侧 删除字典类型
export function typeDelete(
  idList //id
){
  return request({
    url: '/system/api/v1/system/dict/type/delete',
    method: 'get',
    params: {
      idList
    }
  })
}
// 左侧修改字典类型数据
export function updateType(
  description, //字典描述
  id, //id
  name //字典名称

){
  return request({
    url: '/system/api/v1/system/dict/update/type',
    method: 'post',
    data: {
      description, id, name
    }
  })
}
// 右侧 修改字典内容数据
export function updateData(
  description, //字典描述
  id, //id
  label, //字典标签
  sort,
  typeId,
  value
){
  return request({
    url: '/system/api/v1/system/dict/update/data',
    method: 'post',
    data: {
      description, id, label, sort, typeId, value
    }
  })
}

// 系统日志分页列表
export function getSystemLogList(
  current,//  页码
  size,//  每页记录数
  status,//  操作状态
  requestMethod,//  请求方法
  title,//  请求标题
  businessType, //  业务类型
  systemId,//  系统标识
  errorMsg,//  错误信息
  requestData,//  请求数据
  responseData//  响应数据
) {
  return request({
    url: '/system/api/v1/sysOperLog/queryListPage',
    method: 'post',
    data: {
      current,
      size,
      status,
      requestMethod,
      title,
      businessType,
      systemId,
      errorMsg,
      requestData,
      responseData
    }
  });
}
// 词库管理
// 获取词库词语分页
export function eswordPageList(
  current, //第几页，默认：1
  size, //每页记录数，默认：10
  type, //类型：1-扩展词，2-停止词，3-单向同义词，4-双向同义词
  word //词语
){
  return request({
    url: '/system/api/v1/system/esword/pageList',
    method: 'post',
    data: {
      current,
      size,
      type,
      word
    }
  });
}
// 保存词语
export function saveWord(data){
  return request({
    url: '/system/api/v1/system/esword/saveWord',
    method: 'post',
    data: {
      ...data
    }
  })
}
// 词语详情 
export function wordDetail(
  word //词语
){
  return request({
    url: '/system/api/v1/system/esword/wordDetail',
    method: 'get',
    params: {
      word
    }
  })
}
// 同步词库到es搜索引擎 
export function syncWordToEs(
  type // 词语类型
){
  return request({
    url: '/system/api/v1/system/esword/syncWordToEs',
    method: 'get',
    params: {
      type
    }
  })
}
// 更新上传词库oss文件
export function uploadWordDic(
  type // 词语类型
){
  return request({
    url: '/system/api/v1/system/esword/uploadWordDic',
    method: 'get',
    params: {
      type
    }
  })
}
// 获取词库下载地址
export function getDicDownloadUrl(
  type // 词语类型
){
  return request({
    url: '/system/api/v1/system/esword/getDicDownloadUrl',
    method: 'get',
    params: {
      type
    }
  })
}