import request from '@/subject/pop/utils/request'

// 角色列表
export function getCompanyRoleList(
  current,
  size,
  // 角色名
  name,
  // 状态
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/role/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      status
    }
  })
}

// 创建角色信息
export function createRole(
  name,
  remark,
  // 子系统菜单及数据权限列表 {appId: 0, dataScope: 0, menuIds: []}
  rolePermissionList,
  // 	状态：1-启用 2-停用
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/role/create',
    method: 'post',
    data: {
      rolePermissionList,
      name,
      remark,
      status
    }
  })
}

// 修改角色信息
export function updateRole(
  id,
  name,
  remark,
  // 子系统菜单及数据权限列表 {appId: 0, dataScope: 0, menuIds: []}
  rolePermissionList,
  // 	状态：1-启用 2-停用
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/role/update',
    method: 'post',
    data: {
      id,
      name,
      remark,
      rolePermissionList,
      status
    }
  })
}

// 修改角色状态
export function updateRoleStatus(
  id,
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/role/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 获取全部菜单
export function getAllMenus() {
  return request({
    url: '/admin/dataCenter/api/v1/role/getNewRolePageInfo',
    method: 'get',
    params: {
    }
  })
}

// 批量转移角色
export function transferRole(
  id,
  newId
) {
  return request({
    url: '/admin/dataCenter/api/v1/role/moveRoleUsers',
    method: 'post',
    data: {
      id,
      newId
    }
  })
}

// 获取角色下信息
export function getRoleInfo(id) {
  return request({
    url: '/admin/dataCenter/api/v1/role/get',
    method: 'get',
    params: {
      id
    }
  })
}

/**********************   员工管理   ***************************/

// 员工列表
export function getCompanyUserList(
  current,
  size,
  // 手机号
  mobile,
  // 姓名
  name,
  // 状态
  status,
  // 工号
  code,
  // 职位id
  positionId,
  // 部门ID
  departmentId,
  // 员工类型（数据字典：employee_type）
  type,
  //账号id
  userId
) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/pageList',
    method: 'post',
    data: {
      current,
      size,
      mobile,
      name,
      status,
      code,
      positionId,
      departmentId,
      // 员工类型（数据字典：employee_type）
      type,
      userId
    }
  })
}

// 移除员工
export function removeEmployee(userId) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/remove',
    method: 'get',
    params: {
      userId
    }
  })
}

// 创建员工
export function createEmployee(
  mobile,
  name,
  // 上级领导Id
  parentId,
  // 角色ID
  roleId,
  // 职务
  positionId,
  // 工号
  code,
  // 部门ID
  departmentIds,
  // 员工类型（数据字典：employee_type）
  type
) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/create',
    method: 'post',
    data: {
      mobile,
      name,
      parentId,
      roleId,
      positionId,
      code,
      departmentIds,
      // 员工类型（数据字典：employee_type）
      type
    }
  })
}

// 修改员工
export function editEmployee(
  userId,
  // 上级领导Id
  parentId,
  // 角色ID
  roleId,
  // 职务
  positionId,
  // 工号
  code,
  // 部门ID
  departmentIds,
  // 员工类型（数据字典：employee_type）
  type,
  // 姓名
  name
) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/update',
    method: 'post',
    data: {
      userId,
      parentId,
      roleId,
      positionId,
      code,
      departmentIds,
      // 员工类型（数据字典：employee_type）
      type,
      name
    }
  })
}

// 修改员工状态
export function changeEmployeeStatus(
  userId,
  // 	状态：1-启用 2-停用
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/updateStatus',
    method: 'post',
    data: {
      userId,
      status
    }
  })
}

// 获取员工详情
export function getEmployeeInfo(userId) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/get',
    method: 'get',
    params: {
      userId
    }
  })
}

// 检查新员工手机号
export function checkUserTel(mobile) {
  return request({
    url: '/admin/dataCenter/api/v1/employee/checkNewEmployeeMobileNumber',
    method: 'get',
    params: {
      mobile
    }
  })
}

/**********************   部门管理   ***************************/

// 获取企业部门列表
export function getDepartmentList(
  current,
  size,
  // 姓名
  name,
  // 状态
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/department/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      status
    }
  })
}

// 获取部门详情
export function getDepartmentDetail(id) {
  return request({
    url: '/admin/dataCenter/api/v1/department/get',
    method: 'get',
    params: {
      // 部门id
      id
    }
  })
}

// 获取下级部门
export function getDepartmentTreeList(parentId) {
  return request({
    url: '/admin/dataCenter/api/v1/department/listByParentId',
    method: 'get',
    params: {
      parentId
    }
  })
}

// 新增部门
export function addDepartment(
  // 部门
  name,
  // 上级部门ID
  parentId,
  // 部门编码
  code,
  // 部门描述
  description,
  // 部门负责人ID
  managerId
) {
  return request({
    url: '/admin/dataCenter/api/v1/department/add',
    method: 'post',
    data: {
      code,
      description,
      managerId,
      name,
      parentId
    }
  })
}

// 修改部门
export function editDepartment(
  id,
  // 部门
  name,
  // 上级部门ID
  parentId,
  // 部门编码
  code,
  // 部门描述
  description,
  // 部门负责人ID
  managerId
) {
  return request({
    url: '/admin/dataCenter/api/v1/department/update',
    method: 'post',
    data: {
      id,
      code,
      description,
      managerId,
      name,
      parentId
    }
  })
}

// 修改部门状态
export function changeDepartStatus(
  id,
  // 	状态：1-启用 2-停用
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/department/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 批量转移部门员工
export function transferDepart(
  // from
  sourceId,
  // to
  targetId
) {
  return request({
    url: '/admin/dataCenter/api/v1/department/moveEmployee',
    method: 'post',
    data: {
      sourceId,
      targetId
    }
  })
}

/**********************   职位管理   ***************************/

// 获取职位列表
export function getJobList(
  current,
  size,
  // 姓名
  name,
  // 状态
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/position/pageList',
    method: 'post',
    data: {
      current,
      size,
      name,
      status
    }
  })
}

// 修改职位状态
export function changeJobStatus(
  id,
  // 	状态：1-启用 2-停用
  status
) {
  return request({
    url: '/admin/dataCenter/api/v1/position/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

// 新增/修改职位
export function editJob(
  // id
  id,
  // 名字
  name,
  // 描述
  description
) {
  return request({
    url: '/admin/dataCenter/api/v1/position/save',
    method: 'post',
    data: {
      id,
      description,
      name
    }
  })
}

// 获取职位详情
export function getJobDetail(id) {
  return request({
    url: '/admin/dataCenter/api/v1/position/get',
    method: 'get',
    params: {
      // 部门id
      id
    }
  })
}
