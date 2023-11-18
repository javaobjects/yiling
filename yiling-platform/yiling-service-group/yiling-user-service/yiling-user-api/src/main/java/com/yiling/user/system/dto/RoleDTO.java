package com.yiling.user.system.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 角色信息接口
 * @author dexi.yao
 * @date 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleDTO extends BaseDTO {

	/**
	 * 应用ID：1-运营后台 2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手
	 */
	private Integer appId;

	/**
	 * 企业id
	 */
	private Long eid;

	/**
	 * 角色名
	 *
	 */
	private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色类型：1-系统角色 2-自定义角色
     */
    private Integer type;

	/**
	 * 状态：1-启用 2-停用
	 */
	private Integer status;

	/**
	 * 创建人id
	 */
	private Long createUser;

	/**
	 * 更新人id
	 */
	private Long updateUser;

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

}
