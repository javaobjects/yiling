package com.yiling.sjms.sjsp.service;

import com.yiling.sjms.sjsp.dto.DataApproveDeptDTO;
import com.yiling.sjms.sjsp.entity.DataApproveDeptDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 数据审批部门领导对应关系 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-03-03
 */
public interface DataApproveDeptService extends BaseService<DataApproveDeptDO> {

    /**
     * 根据部门名称查询部门对应信息
     *
     * @param deptName
     * @return
     */
    DataApproveDeptDTO getDataApproveDeptByDeptName(String deptName);

    /**
     * 根据部门名称查询部门对应信息
     *
     * @param deptId
     * @return
     */
    DataApproveDeptDTO getDataApproveDeptByDeptId(String deptId);
}
