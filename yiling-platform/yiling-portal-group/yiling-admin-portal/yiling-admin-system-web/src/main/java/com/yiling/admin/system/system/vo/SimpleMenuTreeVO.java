package com.yiling.admin.system.system.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 简单菜单树 VO
 *
 * @author xuan.zhou
 * @date 2022/12/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SimpleMenuTreeVO extends BaseVO {

	/**
	 * 父级id
	 */
	@ApiModelProperty(value = "父类ID", example = "1")
	private Long parentId;

	/**
	 * 权限类型：1-目录 2-菜单 3-按钮
	 */
	@ApiModelProperty(value = "权限类型：1-目录 2-菜单 3-按钮", example = "1")
	private Integer menuType;

	/**
	 * 菜单名称
	 */
	@ApiModelProperty(value = "菜单名称", example = "企业管理")
	private String menuName;

	/**
	 * 菜单或按钮标识
	 */
	@ApiModelProperty(value = "菜单或按钮标识", example = "sys:enterprise")
	private String menuIdentification;

	/**
	 * 菜单图标
	 */
	@ApiModelProperty(value = "菜单图标", example = "http://www/yi.com/icon.jpg")
	private String menuIcon;

	/**
	 * 菜单排序
	 */
	@ApiModelProperty(value = "菜单排序", example = "1")
	private Integer sortNum;

	/**
	 * 菜单状态：1-启用 2-停用
	 */
	@ApiModelProperty(value = "菜单状态：1-启用 2-停用", example = "1")
	private Integer menuStatus;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 子节点列表
	 */
	@ApiModelProperty(value = "子节点列表", example = "1")
	private List<SimpleMenuTreeVO> children;
}
