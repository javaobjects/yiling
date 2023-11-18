package com.yiling.dataflow.sjms.service;

import java.util.List;

import com.yiling.dataflow.sjms.entity.SjmsRoleDatascopeDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 数据洞察系统角色数据权限表 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2023-03-27
 */
public interface SjmsRoleDatascopeService extends BaseService<SjmsRoleDatascopeDO> {

    /**
     * 获取角色对应的数据权限信息列表
     *
     * @param roleIds 角色ID列表
     * @return java.util.List<com.yiling.user.sjms.entity.SjmsRoleDatascopeDO>
     * @author xuan.zhou
     * @date 2023/3/27
     **/
    List<SjmsRoleDatascopeDO> listByRoleIds(List<Long> roleIds);
}
