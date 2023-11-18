package com.yiling.user.enterprise.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.enterprise.dto.request.AddDepartmentRequest;
import com.yiling.user.enterprise.dto.request.MoveDepartmentEmployeeRequest;
import com.yiling.user.enterprise.dto.request.QueryDepartmentPageListRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentRequest;
import com.yiling.user.enterprise.dto.request.UpdateDepartmentStatusRequest;
import com.yiling.user.enterprise.entity.EnterpriseDepartmentDO;
import com.yiling.user.enterprise.entity.EnterpriseEmployeeDepartmentDO;

import cn.hutool.core.lang.tree.Tree;

/**
 * <p>
 * 企业部门信息 服务类
 * </p>
 *
 * @author gxl
 * @date 2021-05-21
 */
public interface EnterpriseDepartmentService extends BaseService<EnterpriseDepartmentDO> {

    /**
     * 保存部门信息
     * @param request
     */
    Boolean add(AddDepartmentRequest request);

    /**
     * 编辑部门信息
     * @param request
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
     * 查询企业部门分页列表
     * @param request
     * @return
     */
    Page<EnterpriseDepartmentDO> pageList(QueryDepartmentPageListRequest request);

    /**
     * 转移部门员工
     *
     * @param request
     * @return
     */
    Boolean moveEmployee(MoveDepartmentEmployeeRequest request);

    /**
     * 递归方式获取指定部门下的所有部门信息
     *
     * @param id 部门ID
     */
    List<EnterpriseDepartmentDO> sublistById(Long id);

    /**
     * 获取下级部门列表
     *
     * @param eid 企业ID
     * @param parentId 上级部门ID
     * @return
     */
    List<EnterpriseDepartmentDO> listByParentId(Long eid, Long parentId);

    /**
     * 根据部门编码查询部门下的用户ID
     * @param code 部门编码
     * @return
     */
    List<EnterpriseEmployeeDepartmentDO> getEmployeeListByCode(String code);

    /**
     * 根据企业id和部门编码获取部门信息
     *
     * @param eid
     * @param code
     * @return
     */
    EnterpriseDepartmentDO getByEidCode(Long eid, String code);

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
