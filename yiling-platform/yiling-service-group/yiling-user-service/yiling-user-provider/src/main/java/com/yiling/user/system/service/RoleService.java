package com.yiling.user.system.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.system.bo.RoleListItemBO;
import com.yiling.user.system.dto.request.CreateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.CreateRoleRequest;
import com.yiling.user.system.dto.request.QueryRolePageListRequest;
import com.yiling.user.system.dto.request.RemoveRoleRequest;
import com.yiling.user.system.dto.request.UpdateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.UpdateRoleInfoRequest;
import com.yiling.user.system.dto.request.UpdateRoleRequest;
import com.yiling.user.system.entity.RoleDO;
import com.yiling.user.system.enums.PermissionAppEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-28
 */
public interface RoleService extends BaseService<RoleDO> {

    /**
     * 根据角色编码获取角色信息
     *
     * @param code 角色编码
     * @return
     */
    RoleDO getByCode(String code);

    /**
     * 根据角色编码列表获取角色信息列表
     *
     * @param codeList 角色编码列表
     * @return
     */
    List<RoleDO> getByCodeList(List<String> codeList);

    /**
     * 分页查询企业下角色
     *
     * @param request
     * @return
     */
    Page<RoleListItemBO> queryRolePageList(QueryRolePageListRequest request);

    /**
     * 分页查询角色管理
     * @param request
     * @return
     */
    Page<RoleListItemBO> queryRoleManagePageList(QueryRolePageListRequest request);

    /**
     * 创建商城自定义角色
     *
     * @param request
     * @return
     */
    boolean createMallCustomRole(CreateMallCustomRoleRequest request);

    /**
     * 修改商城自定义角色
     *
     * @param request
     * @return
     */
    boolean updateMallCustomRole(UpdateMallCustomRoleRequest request);

    /**
     * 创建角色信息
     *
     * @param request
     * @return
     */
    boolean create(CreateRoleRequest request);

    /**
     * 修改角色信息
     *
     * @param request
     * @return
     */
    boolean update(UpdateRoleRequest request);

    /**
     * 修改角色信息
     *
     * @param request
     * @return
     */
    boolean updateRole(UpdateRoleInfoRequest request);

    /**
     * 修改角色状态
     *
     * @param id 角色ID
     * @param statusEnum 角色状态枚举
     * @param opUserId 操作人ID
     * @return
     */
    boolean updateStatus(Long id, EnableStatusEnum statusEnum, Long opUserId);

    /**
     * 获取用户在多个企业下分别拥有的角色列表
     *
     * @param userId 用户ID
     * @param eids 企业ID列表
     * @return key：企业ID， value：角色信息列表
     * @author xuan.zhou
     * @date 2021/6/11
     */
    Map<Long, List<RoleDO>> listByUserIdEids(PermissionAppEnum appEnum, Long userId, List<Long> eids);

    /**
     * 批量获取用户的角色列表
     *
     * @param eid 企业ID
     * @param userIds 用户ID列表
     * @return key：用户ID，value：角色信息列表
     */
    Map<Long, List<RoleDO>> listByEidUserIds(PermissionAppEnum appEnum, Long eid, List<Long> userIds);

    /**
     * 获取用户角色列表
     *
     * @param appEnum 系统类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    List<RoleDO> listByEidUserId(PermissionAppEnum appEnum, Long eid, Long userId);

    /**
     * 获取用户角色列表
     *
     * @param appEnums 系统类型枚举列表
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    List<RoleDO> listByEidUserId(List<PermissionAppEnum> appEnums, Long eid, Long userId);

    /**
     * 获取企业子系统角色列表
     *
     * @param appEnum 系统类型枚举列表
     * @param eid 企业ID
     * @return
     */
    List<RoleDO> listByEid(PermissionAppEnum appEnum, Long eid);

    /**
     * 删除角色
     * @param request
     * @return
     */
    boolean batchDelete(RemoveRoleRequest request);
}
