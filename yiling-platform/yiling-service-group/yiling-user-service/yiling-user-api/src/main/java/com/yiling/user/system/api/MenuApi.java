package com.yiling.user.system.api;

import java.util.List;

import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.user.system.dto.MenuDTO;
import com.yiling.user.system.dto.MenuListItemDTO;
import com.yiling.user.system.dto.request.RemoveMenuRequest;
import com.yiling.user.system.dto.request.SaveMenuRequest;
import com.yiling.user.system.enums.MenuTypeEnum;
import com.yiling.user.system.enums.PermissionAppEnum;

/**
 * @author dexi.yao
 * @date 2021-06-03
 */
public interface MenuApi {

    /**
     * 查询用户在某个系统下的顶级菜单
     *
     * @param appEnum 系统类型枚举
     * @param typeEnums 菜单类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
     * @return
     */
	List<MenuDTO> listMenusByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId);

	/**
	 * 获取用户菜单树
     *
	 * @param appEnum 系统类型枚举
     * @param typeEnums 菜单类型枚举
     * @param eid 企业ID
     * @param userId 用户ID
	 * @return
	 */
	List<MenuListItemDTO> listMenuTreeByUser(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, Long eid, Long userId);

    /**
     * 获取角色绑定的菜单ID列表
     *
     * @param roleIds 角色ID列表
     * @return java.util.List<java.lang.Long>
     * @author xuan.zhou
     * @date 2022/12/8
     **/
    List<Long> listMenuIdsByRoleIds(List<Long> roleIds);

	/**
	 * 获取某个应用的菜单树
     *
	 * @param appEnum 应用类型枚举
	 * @param typeEnums 菜单类型枚举列表
     * @param statusEnum 菜单状态枚举
     * @return
	 */
	List<MenuListItemDTO> listMenuTreeByAppId(PermissionAppEnum appEnum, List<MenuTypeEnum> typeEnums, EnableStatusEnum statusEnum);

	/**
	 * 新增菜单
	 * @param request
	 * @return
	 */
	boolean create(SaveMenuRequest request);

	/**
	 * 修改菜单
	 * @param request
	 * @return
	 */
	boolean update(SaveMenuRequest request);

	/**
	 * 删除菜单
	 * @param request
	 * @return
	 */
	boolean batchDelete(RemoveMenuRequest request);
}
