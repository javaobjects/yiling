package com.yiling.user.system.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 菜单dto
 * @author dexi.yao
 * @date 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MenuListItemDTO extends BaseDTO{

	/**
	 * 父级id
	 */
	private Long parentId;

	/**
	 * 应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手
	 */
	private Integer appId;

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

	/**
	 * 按钮状态：1-禁用 2-移除
	 */
	private Integer buttonType;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 子节点列表
	 */
	private List<MenuListItemDTO> children;

}
