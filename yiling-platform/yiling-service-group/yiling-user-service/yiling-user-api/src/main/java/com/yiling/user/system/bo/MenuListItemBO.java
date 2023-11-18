package com.yiling.user.system.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.yiling.user.system.dto.MenuListItemDTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-02
 */
@Data
@Accessors
public class MenuListItemBO implements Comparable<MenuListItemBO>, Serializable {

	private Long id;

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
	private List<MenuListItemBO> children;

	@Override
	public int compareTo(MenuListItemBO o) {
		return this.sortNum.compareTo(o.sortNum);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o){
			return true;
		}
		if (!(o instanceof MenuListItemDTO)){
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		MenuListItemBO that = (MenuListItemBO) o;
		return Objects.equals(this.getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getId());
	}
}
