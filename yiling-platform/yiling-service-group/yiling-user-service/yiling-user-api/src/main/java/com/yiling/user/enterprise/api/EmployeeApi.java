package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.CreateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryEmployeePageListRequest;
import com.yiling.user.enterprise.dto.request.RemoveEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeRequest;
import com.yiling.user.enterprise.dto.request.UpdateEmployeeStatusRequest;
import com.yiling.user.system.bo.Staff;

/**
 * 企业员工 API
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
public interface EmployeeApi {

    /**
     * 查询员工分页列表
     *
     * @param request
     * @return
     */
    Page<EnterpriseEmployeeDTO> pageList(QueryEmployeePageListRequest request);

    /**
     * 根据员工ID获取员工信息
     *
     * @param id 员工ID
     * @return com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO
     * @author xuan.zhou
     * @date 2022/6/6
     **/
    EnterpriseEmployeeDTO getById(Long id);

    /**
     * 批量根据员工ID获取员工信息
     *
     * @param ids 员工ID列表
     * @return java.util.List<com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO>
     * @author xuan.zhou
     * @date 2022/6/7
     **/
    List<EnterpriseEmployeeDTO> listByIds(List<Long> ids);

    /**
     * 保存员工信息
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
     * @param request
     * @return
     */
    boolean remove(RemoveEmployeeRequest request);

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
    List<Staff> listAdminsByEid(Long eid);

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
     * 批量获取某个企业下用户对应的员工信息列表
     *
     * @param eid 企业ID
     * @param userIds 用户ID列表
     * @return
     */
    List<EnterpriseEmployeeDTO> listByEidUserIds(Long eid, List<Long> userIds);

    /**
     * 获取用户对应的所有员工信息列表
     *
     * @param userId 用户ID
     * @param statusEnum 状态枚举
     * @return
     */
    List<EnterpriseEmployeeDTO> listByUserId(Long userId, EnableStatusEnum statusEnum);

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
    List<EnterpriseEmployeeDTO> listByUserIdEids(Long userId, List<Long> eids, EnableStatusEnum statusEnum);

    /**
     * 获取企业对应的员工列表
     *
     * @param eid 企业ID
     * @param statusEnum 状态枚举
     * @return java.util.List<com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO>
     * @author xuan.zhou
     * @date 2022/4/15
     **/
    List<EnterpriseEmployeeDTO> listByEid(Long eid, EnableStatusEnum statusEnum);

    /**
     * 获取员工信息
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    EnterpriseEmployeeDTO getByEidUserId(Long eid, Long userId);

    /**
     * 根据员工工号获取员工信息
     *
     * @param eid 企业ID
     * @param code 员工工号
     * @return
     */
    EnterpriseEmployeeDTO getByCode(Long eid, String code);

    /**
     * 获取员工上级领导信息
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    EnterpriseEmployeeDTO getParentInfo(Long eid, Long userId);

    /**
     * 获取用户所属部门ID列表
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    List<Long> listDepartmentIdsByUser(Long eid, Long userId);

    /**
     * 批量获取员工所属部门ID列表
     *
     * @param employeeIds 员工ID列表
     * @return key：员工ID，value：所属部门ID列表
     */
    Map<Long, List<Long>> listDepartmentIdsByEmployeeIds(List<Long> employeeIds);
}
