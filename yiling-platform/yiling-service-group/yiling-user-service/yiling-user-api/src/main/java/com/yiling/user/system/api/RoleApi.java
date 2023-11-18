package com.yiling.user.system.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.system.dto.RoleDTO;
import com.yiling.user.system.dto.RoleDataScopeDTO;
import com.yiling.user.system.dto.RoleListItemDTO;
import com.yiling.user.system.dto.request.CreateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.CreateRoleRequest;
import com.yiling.user.system.dto.request.MoveRoleUsersRequest;
import com.yiling.user.system.dto.request.QueryRolePageListRequest;
import com.yiling.user.system.dto.request.RemoveRoleRequest;
import com.yiling.user.system.dto.request.RoleMenuRequest;
import com.yiling.user.system.dto.request.UpdateMallCustomRoleRequest;
import com.yiling.user.system.dto.request.UpdateRoleInfoRequest;
import com.yiling.user.system.dto.request.UpdateRoleRequest;
import com.yiling.user.system.enums.PermissionAppEnum;

/**
 * 用户权限api
 * @author dexi.yao
 * @date 2021-05-31
 */
public interface RoleApi {

	/**
	 * 分页查询企业下角色
     *
	 * @param request
	 * @return
	 */
	Page<RoleListItemDTO> queryRolePageList(QueryRolePageListRequest request);

	/**
	 * 分页查询角色管理
	 *
	 * @param request
	 * @return
	 */
	Page<RoleListItemDTO> queryRoleManagePageList(QueryRolePageListRequest request);


	/**
	 * 根据ID获取角色信息
     *
	 * @param id 角色ID
	 * @return
	 */
	RoleDTO getById(Long id);

	/**
	 * 批量根据ID获取角色信息
     *
	 * @param ids 角色ID列表
	 * @return
	 */
	List<RoleDTO> listByIds(List<Long> ids);

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
     * @param appEnum 系统类型枚举
     * @param userId 用户ID
     * @param eids 企业ID列表
     * @return key：企业ID， value：角色信息列表
     * @author xuan.zhou
     * @date 2021/6/11
     */
    Map<Long, List<RoleDTO>> listByUserIdEids(PermissionAppEnum appEnum, Long userId, List<Long> eids);

    /**
     * 批量获取用户的角色列表
     *
     * @param eid 企业ID
     * @param userIds 用户ID列表
     * @return key：用户ID，value：角色信息列表
     */
    Map<Long, List<RoleDTO>> listByEidUserIds(PermissionAppEnum appEnum, Long eid, List<Long> userIds);

    /**
     * 获取用户在某个企业下的角色列表
     *
     * @param appEnum 系统类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
    List<RoleDTO> listByEidUserId(PermissionAppEnum appEnum, Long eid, Long userId);

    /**
     * 转移角色人员
     *
     * @param request
     * @return
     */
    boolean moveRoleUsers(MoveRoleUsersRequest request);

	/**
	 * 创建角色对应菜单
	 * @param request
	 * @return
	 */
	boolean createRoleMenu(RoleMenuRequest request);

	/**
	 * 更新角色对应菜单
	 * @param request
	 * @return
	 */
	boolean updateRoleMenu(RoleMenuRequest request);

	/**
	 * 获取所有运营后台的角色
	 * @return
	 */
	List<RoleDTO> allRole();

	/**
	 * 删除角色
	 * @param request
	 * @return
	 */
    boolean batchDelete(RemoveRoleRequest request);

    /**
     * 获取某个角色对应不同子系统的数据权限集合
     *
     * @param roleId 角色ID
     * @return
     */
    Map<Integer, RoleDataScopeDTO> getRoleDataScopeMap(Long roleId);
}
