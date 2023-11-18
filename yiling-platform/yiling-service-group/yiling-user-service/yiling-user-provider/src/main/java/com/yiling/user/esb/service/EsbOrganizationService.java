package com.yiling.user.esb.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.esb.bo.EsbOrgInfoTreeBO;
import com.yiling.user.esb.bo.SimpleEsbOrgInfoBO;
import com.yiling.user.esb.dto.request.SaveOrUpdateEsbOrganizationRequest;
import com.yiling.user.esb.entity.EsbOrganizationDO;

/**
 * <p>
 * esb组织架构 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-24
 */
public interface EsbOrganizationService extends BaseService<EsbOrganizationDO> {

    public static final String ORG_STATE_NORMAL = "0";
    public static final String ORG_STATUS_NORMAL = "0";

    /**
     * 根据部门ID获取部门信息
     *
     * @param orgId 部门ID
     * @return com.yiling.user.esb.entity.EsbOrganizationDO
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    EsbOrganizationDO getByOrgId(Long orgId);

    /**
     * 批量根据部门ID获取部门信息
     *
     * @param orgIds 部门ID列表
     * @return java.util.List<com.yiling.user.esb.entity.EsbOrganizationDO>
     * @author xuan.zhou
     * @date 2023/3/9
     **/
    List<EsbOrganizationDO> listByOrgIds(List<Long> orgIds);

    /**
     * 根据上级部门ID和部门名称获取部门信息
     *
     * @param orgPid 上级部门ID
     * @param orgName 部门名称
     * @return com.yiling.user.esb.entity.EsbOrganizationDO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    EsbOrganizationDO getByPidAndName(Long orgPid, String orgName);


    /**
     * 根据上级部门ID集合和部门名称获取部门信息
     * @param orgPidList
     * @param orgName
     * @return
     */
    EsbOrganizationDO getByPidListAndName(List<Long> orgPidList, String orgName);

    /**
     * 根据上级部门ID获取下级部门信息列表
     *
     * @param orgPid 上级部门ID
     * @return java.util.List<com.yiling.user.esb.entity.EsbOrganizationDO>
     * @author xuan.zhou
     * @date 2023/2/15
     **/
    List<EsbOrganizationDO> listByPid(Long orgPid);

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
     * 根据上级部门ID和部门名称获取部门信息
     *
     * @param orgPid 上级部门ID
     * @return com.yiling.user.esb.entity.EsbOrganizationDO
     * @author xuan.zhou
     * @date 2022/11/28
     **/
    EsbOrganizationDO getByPid(Long orgPid);

    /**
     * 保存或更新部门信息
     *
     * @param request
     * @return java.lang.Boolean
     * @author xuan.zhou
     * @date 2022/11/25
     **/
    Boolean saveOrUpdate(SaveOrUpdateEsbOrganizationRequest request);


    /**
     * 部门名称模糊查询部门列表
     * @param orgName
     * @return
     */
    List<EsbOrganizationDO> findByOrgName(String orgName, String tableSuffix);

    /**
     * 获取ESB部门架构树结构
     *
     * @return 数据树
     */
    List<EsbOrgInfoTreeBO> listTree();


    /**
     * 批量根据部门ID获取部门信息 查备份表
     *
     * @param orgIds 部门ID列表
     * @return java.util.List<com.yiling.user.esb.entity.EsbOrganizationDO>
     * @author shixing.sun
     * @date 2023/05/10
     **/
    List<EsbOrganizationDO> listSuffixByOrgIds(List<Long> orgIds,String tableSuffix);

    /**
     * 获取所有部门信息（简单信息） 查备份表数据
     *
     * @param setParentOrgInfoFlag 是否设置上级部门信息
     * @return java.util.List<com.yiling.user.esb.bo.SimpleEsbOrgInfoBO>
     * @author xuan.zhou
     * @date 2023/3/9
     **/
    List<SimpleEsbOrgInfoBO> listSuffixAll(boolean setParentOrgInfoFlag,String tableSuffix);
}
