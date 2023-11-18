package com.yiling.sjms.gb.api;

import java.util.List;

import com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO;
import com.yiling.user.esb.dto.request.QueryProvinceManagerRequest;

/**
 * 团购工作流处理人 API
 *
 * @author: xuan.zhou
 * @date: 2022/11/28
 */
public interface GbWorkflowProcessorApi {

    /**
     * 获取部门对应省区的省区经理信息
     *
     * @param request
     * @return com.yiling.user.esb.bo.SimpleEsbEmployeeInfoBO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    SimpleEsbEmployeeInfoBO getByProvinceName(QueryProvinceManagerRequest request);

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
}
