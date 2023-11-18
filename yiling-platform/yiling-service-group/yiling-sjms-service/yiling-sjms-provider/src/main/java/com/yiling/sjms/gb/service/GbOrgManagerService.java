package com.yiling.sjms.gb.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.gb.entity.GbOrgManagerDO;
import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;

/**
 * <p>
 * 业务部负责人关系 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-28
 */
public interface GbOrgManagerService extends BaseService<GbOrgManagerDO> {
    
    /**
     * 获取业务部负责人信息
     *
     * @param orgId 部门ID
     * @return com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    SimpleEsbEmployeeInfoBO getByOrgId(Long orgId);

    /**
     * 批量获取业务部负责人信息
     *
     * @param orgIds 部门ID列表
     * @return java.util.List<com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO>
     * @author xuan.zhou
     * @date 2023/1/11
     **/
    List<SimpleEsbEmployeeInfoBO> listByOrgIds(List<Long> orgIds);

    /**
     * 获取事业部部门ID列表
     *
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2023/3/3
     **/
    List<Long> listOrgIds();
}
