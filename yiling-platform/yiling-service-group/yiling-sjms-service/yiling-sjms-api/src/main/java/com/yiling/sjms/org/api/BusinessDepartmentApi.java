package com.yiling.sjms.org.api;

import java.util.List;
import java.util.Map;

import com.yiling.user.esb.dto.EsbOrganizationDTO;

/**
 * 事业部 API
 *
 * @author: xuan.zhou
 * @date: 2023/3/3
 */
public interface BusinessDepartmentApi {

    /**
     * 根据员工ID获取员工对应的事业部信息
     *
     * @param empId 员工ID
     * @return com.yiling.user.esb.dto.EsbOrganizationDTO
     * @author xuan.zhou
     * @date 2023/3/3
     **/
    EsbOrganizationDTO getByEmpId(String empId);

    /**
     * 批量根据员工ID获取员工对应的事业部信息
     *
     * @param empIds 员工ID列表
     * @return java.util.Map<java.lang.String,com.yiling.user.esb.dto.EsbOrganizationDTO>
     * @author xuan.zhou
     * @date 2023/3/10
     **/
    Map<String, EsbOrganizationDTO> listByEmpIds(List<String> empIds);

    /**
     * 根据岗位ID获取岗位对应的事业部信息
     *
     * @param jobId 岗位ID
     * @return com.yiling.user.esb.dto.EsbOrganizationDTO
     * @author xuan.zhou
     * @date 2023/3/3
     **/
    EsbOrganizationDTO getByJobId(Long jobId);

    /**
     * 批量根据岗位ID获取岗位对应的事业部信息
     *
     * @param jobIds 岗位ID列表
     * @return java.util.Map<java.lang.Long,com.yiling.user.esb.dto.EsbOrganizationDTO>
     * @author xuan.zhou
     * @date 2023/3/10
     **/
    Map<Long, EsbOrganizationDTO> listByJobIds(List<Long> jobIds);

    /**
     * 根据部门ID获取部门对应的事业部信息
     *
     * @param orgId 部门ID
     * @return com.yiling.user.esb.dto.EsbOrganizationDTO
     * @author xuan.zhou
     * @date 2023/3/3
     **/
    EsbOrganizationDTO getByOrgId(Long orgId);

    /**
     * 批量根据部门ID获取部门对应的事业部信息
     *
     * @param orgIds 部门ID列表
     * @return java.util.Map<java.lang.Long,com.yiling.user.esb.dto.EsbOrganizationDTO>
     * @author xuan.zhou
     * @date 2023/3/10
     **/
    Map<Long, EsbOrganizationDTO> listByOrgIds(List<Long> orgIds);
}
