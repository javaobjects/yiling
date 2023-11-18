package com.yiling.user.esb.api;

import java.util.List;

import com.yiling.user.esb.bo.EsbOrgInfoTreeBO;
import com.yiling.user.esb.bo.SimpleEsbOrgInfoBO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbOrganizationRequest;

/**
 * ESB组织架构 API
 *
 * @author: xuan.zhou
 * @date: 2022/11/25
 */
public interface EsbOrganizationApi {

    /**
     * 保存或更新组织架构信息
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    Boolean saveOrUpdate(SaveOrUpdateEsbOrganizationRequest request);

    /**
     * 根据部门ID获取部门信息
     *
     * @param orgId 部门ID
     * @return com.yiling.user.esb.dto.EsbOrganizationDTO
     * @author xuan.zhou
     * @date 2023/3/3
     **/
    EsbOrganizationDTO getByOrgId(Long orgId);

    /**
     * 批量根据部门ID获取部门信息
     *
     * @param orgIds 部门ID列表
     * @return java.util.List<com.yiling.user.esb.entity.EsbOrganizationDO>
     * @author xuan.zhou
     * @date 2023/3/9
     **/
    List<EsbOrganizationDTO> listByOrgIds(List<Long> orgIds);

    /**
     * 根据上级部门ID和部门名称获取部门信息
     *
     * @param orgPid 上级部门ID
     * @param orgName 部门名称
     * @return com.yiling.user.esb.entity.EsbOrganizationDO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    EsbOrganizationDTO getByPidAndName(Long orgPid, String orgName);

    /**
     * 根据上级部门ID和部门名称获取部门信息
     *
     * @param orgPid 上级部门ID
     * @return com.yiling.user.esb.entity.EsbOrganizationDO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    EsbOrganizationDTO getByPid(Long orgPid);
    /*
     * 根据上级部门ID集合和部门名称获取部门信息
     * @param orgPidList
     * @param orgName
     * @return
     */
    EsbOrganizationDTO getByPidListAndName(List<Long> orgPidList, String orgName);


    /**
     * 根据上级部门ID获取下级部门信息列表
     *
     * @param orgPid 上级部门ID
     * @return java.util.List<com.yiling.user.esb.entity.EsbOrganizationDO>
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    List<EsbOrganizationDTO> listByPid(Long orgPid);

    /**
     * 获取所有部门信息（简单信息）
     *
     * @param setParentOrgInfoFlag 是否设置上级部门信息
     * @return java.util.List<com.yiling.user.esb.bo.SimpleEsbOrgInfoBO>
     * @author xuan.zhou
     * @date 2023/3/9
     **/
    List<SimpleEsbOrgInfoBO> listAll(boolean setParentOrgInfoFlag);

    /**
     * 部门名称模糊查询部门列表
     * @param orgName,  tableSuffix 备份表后缀 wash_yyyy_mm
     * @return
     */
    List<EsbOrganizationDTO> findByOrgName(String orgName, String tableSuffix);

    /**
     * 获取ESB部门架构树结构
     *
     * @return 数据树
     */
    List<EsbOrgInfoTreeBO> listTree();

}
