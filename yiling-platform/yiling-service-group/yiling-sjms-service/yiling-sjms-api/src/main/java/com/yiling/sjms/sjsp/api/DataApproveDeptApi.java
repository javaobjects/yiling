package com.yiling.sjms.sjsp.api;

import com.yiling.sjms.sjsp.dto.DataApproveDeptDTO;

/**
 * 数据审核板块与部门信息对应
 *
 * @author: dexi.yao
 * @date: 2022/11/28
 */
public interface DataApproveDeptApi {

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
