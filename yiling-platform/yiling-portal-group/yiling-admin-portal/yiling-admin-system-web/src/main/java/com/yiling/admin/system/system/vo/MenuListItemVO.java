package com.yiling.admin.system.system.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MenuListItemVO extends BaseVO {

	/**
	 * 父级id
	 */
	@ApiModelProperty(value = "父类ID", example = "1")
	private Long parentId;

	/**
	 * 应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手 9-健康管理中心
	 */
	@ApiModelProperty(value = "应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手 9-健康管理中心", example = "1")
	private Integer appId;

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
	 * 接口url
	 */
	@ApiModelProperty(value = "接口url", example = "/test/test")
	private String menuUrl;

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
	 * 按钮状态：1-禁用 2-移除
	 */
	@ApiModelProperty(value = "按钮状态：1-禁用 2-移除")
	private Integer buttonType;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty("更新时间")
	private Date updateTime;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 子节点列表
	 */
	@ApiModelProperty(value = "子节点列表", example = "1")
	private List<MenuListItemVO> children;
}
