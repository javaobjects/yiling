package com.yiling.admin.system.system.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色菜单VO
 *
 * @author: lun.yu
 * @date: 2021/7/26
 */
@Data
public class RoleMenuVO {

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
        private List<RoleMenuVO> children;
}