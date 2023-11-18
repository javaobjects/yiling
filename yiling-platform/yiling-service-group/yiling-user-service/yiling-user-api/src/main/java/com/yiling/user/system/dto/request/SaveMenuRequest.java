package com.yiling.user.system.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建菜单 Request
 *
 * @author lun.yu
 * @date 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMenuRequest extends BaseRequest {

	/**
	 * 菜单ID
	 */
	private Long id;

	/**
     * 应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手
	 */
	private Integer appId;

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

    /**
     * 备注
	 */
	private String remark;

}
