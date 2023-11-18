package com.yiling.user.enterprise.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDepartmentDTO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;

/**
 * <p>
 * 企业员工所属部门信息 服务类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-11-05
 */
public interface EnterpriseEmployeeDepartmentService extends BaseService<EnterpriseEmployeeDepartmentDO> {

    /**
     * 根据员工ID获取员工所属部门信息列表
     *
     * @param employeeId 员工ID
     * @return
     */
    List<EnterpriseEmployeeDepartmentDO> listByEmployeeId(Long employeeId);

    /**
     * 根据部门ID获取员工所属部门信息列表
     *
     * @param departmentId 部门ID
     * @return
     */
    List<EnterpriseEmployeeDepartmentDO> listByDepartmentId(Long departmentId);

    /**
     * 获取部门下的员工ID列表
     *
     * @param departmentIds 部门ID列表
     * @return
     */
    List<Long> listUserIdsByDepartmentIds(List<Long> departmentIds);

    /**
     * 获取用户所属部门ID列表
     *
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    List<Long> listByUser(Long eid, Long userId);

    /**
     * 批量获取员工所属部门ID列表
     *
     * @param employeeIds 员工ID列表
     * @return key：员工ID，value：所属部门ID列表
     */
    Map<Long, List<Long>> listByEmployeeIds(List<Long> employeeIds);

    /**
     * 保存员工部门信息
     *
     * @param employeeId 员工ID
     * @param departmentIds 所属部门ID列表
     * @param opUserId 操作人ID
     * @return
     */
    boolean saveEmployeeDepartments(Long employeeId, List<Long> departmentIds, Long opUserId);

    /**
     * 批量根据某个企业的用户，获取每个用户对应的部门信息
     *
     * @param eid
     * @param userIdList
     * @return
     */
    Map<Long, List<EnterpriseEmployeeDepartmentDO>> getDepartmentByEidUser(Long eid, List<Long> userIdList);
}
