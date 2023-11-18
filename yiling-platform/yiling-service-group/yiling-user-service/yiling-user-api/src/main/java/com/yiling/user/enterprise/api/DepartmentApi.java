package com.yiling.user.enterprise.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDepartmentDTO;
import com.yiling.user.enterprise.dto.request.AddDepartmentRequest;
import com.yiling.user.enterprise.dto.request.MoveDepartmentEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryDepartmentPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentStatusRequest;

import cn.hutool.core.lang.tree.Tree;

/**
 * 部门api
 * @author: ray
 * @date: 2021/5/27
 */
public interface DepartmentApi {

    /**
     * 根据ID获取企业部门信息
     *
     * @param id 部门ID
     * @return
     */
    EnterpriseDepartmentDTO getById(Long id);

    /**
     * 批量根据ID获取企业部门信息
     *
     * @param ids 部门ID列表
     * @return
     */
    List<EnterpriseDepartmentDTO> listByIds(List<Long> ids);

    /**
     * 添加部门信息
     *
     * @param request
     */
    Boolean add(AddDepartmentRequest request);

    /**
     * 修改部门信息
     *
     * @param request
     * @return
     */
    Boolean update(UpdateDepartmentRequest request);

    /**
     * 修改部门状态
     *
     * @param request
     * @return
     */
    Boolean updateStatus(UpdateDepartmentStatusRequest request);

    /**
     * 获取企业部门分页列表
     * @param request
     * @return
     */
    Page<EnterpriseDepartmentDTO> pageList(QueryDepartmentPageListRequest request);

    /**
     * 转移部门员工
     *
     * @param request
     * @return
     */
    Boolean moveEmployee(MoveDepartmentEmployeeRequest request);

    /**
     * 获取下级部门列表
     *
     * @param eid 企业ID
     * @param parentId 上级部门ID
     * @return
     */
    List<EnterpriseDepartmentDTO> listByParentId(Long eid, Long parentId);

    /**
     * 根据员工ID获取员工所属部门信息列表
     * @param employeeId
     * @return
     */
    List<EnterpriseEmployeeDepartmentDTO> listByEmployeeId(Long employeeId);

    /**
     * 根据部门编码查询部门下的用户ID
     * @param code 部门编码
     * @return
     */
    List<EnterpriseEmployeeDepartmentDTO> getEmployeeListByCode(String code);

    /**
     * 批量根据某个企业的用户，获取每个用户对应的部门信息
     *
     * @param eid 企业ID
     * @param userIdList 用户ID集合
     * @return key为userId，value为每个用户对应的部门信息
     */
    Map<Long, List<EnterpriseDepartmentDTO>> getDepartmentByEidUser(Long eid, List<Long> userIdList);

    /**
     * 根据企业id和部门编码获取部门信息
     *
     * @param eid 企业ID
     * @param code 部门编码
     * @return
     */
    EnterpriseDepartmentDTO getByEidCode(Long eid, String code);

    /**
     * 获取企业部门树
     *
     * @param eid 企业ID
     * @param statusEnum 状态枚举
     * @return java.util.List<cn.hutool.core.lang.tree.Tree<java.lang.Long>>
     * @author xuan.zhou
     * @date 2023/1/9
     **/
    List<Tree<Long>> listTreeByEid(Long eid, EnableStatusEnum statusEnum);

}
