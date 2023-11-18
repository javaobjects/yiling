package com.yiling.dataflow.sjms.service;

import java.util.List;

import com.yiling.dataflow.sjms.entity.SjmsRoleDatascopeProvinceDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 数据洞察系统角色数据权限明细 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-03-27
 */
public interface SjmsRoleDatascopeProvinceService extends BaseService<SjmsRoleDatascopeProvinceDO> {

    /**
     * 根据角色ID、员工工号获取管理的省区名称列表
     *
     * @param roleId 角色ID
     * @param empId 员工工号
     * @return java.util.List<java.lang.String>
     * @author xuan.zhou
     * @date 2023/3/27
     **/
    List<String> listProvinceNamesByRoleIdEmpId(Long roleId, String empId);
}
