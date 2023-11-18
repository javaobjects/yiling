package com.yiling.user.system.bo;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-31
 */
@Data
@Accessors(chain = true)
public class RoleListItemBO {

	/**
	 * 角色id
	 */
	private Long id;

	/**
	 * 角色名称
	 */
	private String name;

    /**
     * 角色编码
     */
    private String code;

	/**
	 * 角色类型：1-系统角色 2-自定义角色
	 */
	private String type;

	/**
	 * 状态：1-启用 2-停用
	 */
	private Integer status;

    /**
     * 创建人
     */
    private Long createUser;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	private Long updateUser;

	/**
	 * 更新时间
	 */
	private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 使用此角色的用户数
     */
    private Integer userCount;

}
