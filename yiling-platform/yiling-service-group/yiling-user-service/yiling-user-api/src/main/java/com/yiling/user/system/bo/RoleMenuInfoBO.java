package com.yiling.user.system.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色下菜单dto
 * @author dexi.yao
 * @date 2021-06-01
 */
@Data
@Accessors(chain = true)
public class RoleMenuInfoBO {


	/**
	 * 角色Id
	 */
	private Long roleId;

	/**
	 * 父级id
	 */
	private Long parentId;

	/**
	 * 权限类型：1-目录 2-菜单 3-按钮
	 */
	private Integer menuType;

	/**
	 * 菜单名称
	 */
	private String menuName;

	/**
	 * 接口url
	 */
	private String menuUrl;

	/**
	 * 菜单或按钮标识
	 */
	private String menuIdentification;

	/**
	 * 菜单图标
	 */
	private String menuIcon;

	/**
	 * 菜单排序
	 */
	private Integer sortNum;

	/**
	 * 菜单状态：1-启用 2-停用
	 */
	private Integer menuStatus;

}
