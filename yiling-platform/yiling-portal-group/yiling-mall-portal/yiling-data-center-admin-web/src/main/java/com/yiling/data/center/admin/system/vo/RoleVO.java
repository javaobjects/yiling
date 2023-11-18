package com.yiling.data.center.admin.system.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleVO extends BaseVO {

	/**
	 * 角色名
	 */
	@ApiModelProperty(value = "角色名", example = "角色名")
	private String name;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注", example = "备注")
	private String remark;

	/**
	 * 状态：1-启用 2-停用
	 */
	@ApiModelProperty(value = "状态：1-启用 2-停用", example = "1")
	private Integer status;

	/**
	 * 子系统权限信息列表
	 */
	@ApiModelProperty(value = "子系统权限信息列表")
	private List<SubsystemPermissionVO> systemPermissionList;

    @Data
    public static class SubsystemPermissionVO {

        @ApiModelProperty("应用ID：1-运营后台 2-POP管理 3-B2B管理 4-互联网医院 5-企业数据管理 6-销售助手 9-健康管理中心 10-神机妙算系统")
        private Integer appId;

        @ApiModelProperty("菜单集合")
        private List<MenuTreeVO> menuList;

        @ApiModelProperty("是否配置数据范围")
        private Boolean dataScopeFlag;

        @ApiModelProperty("数据范围：0-未定义 1-本人 2-本部门 3-本部门及下级部门 4-全部数据")
        private Integer dataScope;
    }

	@Data
	public static class MenuTreeVO {

        /**
         * 菜单ID
         */
        @ApiModelProperty(value = "菜单ID", example = "1")
        private Long id;

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
         * 菜单图标
         */
        @ApiModelProperty(value = "菜单图标", example = "http://www/yi.com/icon.jpg")
        private String menuIcon;

        /**
         * 是否选中
         */
        @ApiModelProperty(value = "是否选中")
        private Boolean selected;

        /**
         * 子节点列表
         */
        @ApiModelProperty(value = "子节点列表", example = "1")
        private List<MenuTreeVO> children;
    }
}
