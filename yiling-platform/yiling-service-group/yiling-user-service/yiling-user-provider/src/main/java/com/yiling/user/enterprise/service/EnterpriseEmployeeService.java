package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.CreateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeStatusRequest;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDO;
import com.yiling.user.system.bo.MrBO;
import com.yiling.user.system.dto.request.QueryMrPageListRequest;

/**
 * <p>
 * 企业员工信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-05-26
 */
public interface EnterpriseEmployeeService extends BaseService<EnterpriseEmployeeDO> {

    /**
     * 查询员工分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseEmployeeDTO> pageList(QueryEmployeePageListRequest request);

    /**
     * 查询企业医药代表分页列表
     *
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.user.system.bo.MrBO>
     * @author xuan.zhou
     * @date 2022/6/7
     **/
    Page<MrBO> mrPageList(QueryMrPageListRequest request);

    /**
     * 批量根据医药代表ID获取医药代表信息
     *
     * @param ids 医药代表ID列表
     * @return java.util.List<com.yiling.user.system.bo.MrBO>
     * @author xuan.zhou
     * @date 2022/6/9
     **/
    List<MrBO> mrListByIds(List<Long> ids);

    /**
     * 根据医药代表ID获取医药代表信息
     *
     * @param id 医药代表ID
     * @return com.yiling.user.system.bo.MrBO
     * @author xuan.zhou
     * @date 2022/6/9
     **/
    MrBO getMrById(Long id);

    /**
     * 创建员工信息
     *
     * @param request
     * @return
     */
    long create(CreateEmployeeRequest request);

    /**
     * 修改员工信息
     *
     * @param request
     * @return
     */
    boolean update(UpdateEmployeeRequest request);

    /**
     * 修改员工状态
     *
     * @param request
     * @return
     */
    boolean updateStatus(UpdateEmployeeStatusRequest request);

    /**
     * 移除员工
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @param opUserId 操作人ID
     * @return
     */
    boolean remove(Long eid, Long userId, Long opUserId);

    /**
     * 获取用户对应的所有员工信息列表
     *
     * @param userId 用户ID
     * @param statusEnum 状态枚举
     * @return
     */
    List<EnterpriseEmployeeDO> listByUserId(Long userId, EnableStatusEnum statusEnum);

    /**
     * 获取员工对应企业的员工信息列表
     *
     * @param userId 用户ID
     * @param eids 企业ID列表
     * @param statusEnum 状态枚举
     * @return java.util.List<com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO>
     * @author xuan.zhou
     * @date 2022/12/6
     **/
    List<EnterpriseEmployeeDO> listByUserIdEids(Long userId, List<Long> eids, EnableStatusEnum statusEnum);

    /**
     * 获取企业对应的员工列表
     *
     * @param eid 企业ID
     * @param statusEnum 状态枚举
     * @return java.util.List<com.yiling.user.enterprise.entity.EnterpriseEmployeeDO>
     * @author xuan.zhou
     * @date 2022/3/9
     **/
    List<EnterpriseEmployeeDO> listByEid(Long eid, EnableStatusEnum statusEnum);

    /**
     * 批量获取用户对应的所有员工信息列表
     *
     * @param userIds 用户ID列表
     * @return
     */
    List<EnterpriseEmployeeDO> listByUserIds(List<Long> userIds);

    /**
     * 批量获取某个企业下用户对应的员工信息列表
     *
     * @param eid 企业ID
     * @param userIds 用户ID列表
     * @return
     */
    List<EnterpriseEmployeeDO> listByEidUserIds(Long eid, List<Long> userIds);

    /**
     * 判断员工是否存在于某个企业中
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    boolean exists(Long eid, Long userId);

    /**
     * 获取企业的管理员信息列表
     *
     * @param eid 企业ID
     * @return
     */
    List<EnterpriseEmployeeDO> listAdminsByEid(Long eid);

    /**
     * 判断用户是否是企业的管理员
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    boolean isAdmin(Long eid, Long userId);

    /**
     * 统计企业账号数量
     *
     * @param eids 企业ID列表
     * @return key：企业ID，value：企业账号数量
     */
    Map<Long, Long> countByEids(List<Long> eids);

    /**
     * 统计部门下的员工人数
     *
     * @param departmentId 部门ID
     * @return
     */
    Integer countByDepartmentId(Long departmentId);

    /**
     * 统计部门下的员工人数
     *
     * @param departmentIds 部门ID列表
     * @return
     */
    Map<Long, Long> countByDepartmentIds(List<Long> departmentIds);

    /**
     * 统计职位下的员工人数
     *
     * @param positionId 职位ID
     * @return
     */
    Integer countByPositionId(Long positionId);

    /**
     * 统计职位下的员工人数
     *
     * @param positionIds 职位ID列表
     * @return
     */
    Map<Long, Long> countByPositionIds(List<Long> positionIds);

    /**
     * 获取员工信息
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    EnterpriseEmployeeDO getByEidUserId(Long eid, Long userId);

    /**
     * 根据工号获取员工信息
     *
     * @param eid 企业ID
     * @param code 员工工号
     * @return
     */
    EnterpriseEmployeeDO getByCode(Long eid, String code);

    /**
     * 获取员工上级领导信息
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    EnterpriseEmployeeDO getParentInfo(Long eid, Long userId);

}
