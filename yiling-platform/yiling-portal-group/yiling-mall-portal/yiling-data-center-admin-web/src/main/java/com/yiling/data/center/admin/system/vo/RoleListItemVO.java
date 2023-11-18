package com.yiling.data.center.admin.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RoleListItemVO extends BaseVO {

	/**
	 * 角色名称
	 */
	@ApiModelProperty(value = "角色名称")
	private String name;

    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码")
    private String code;

	/**
	 * 角色类型：1-系统角色 2-自定义角色
	 */
	@ApiModelProperty(value = "角色类型：1-系统角色 2-自定义角色")
	private String type;

	/**
	 * 状态：1-启用 2-停用
	 */
	@ApiModelProperty(value = "状态：1-启用 2-停用")
	private Integer status;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID")
    private Long createUser;

    /**
     * 创建人姓名
     */
    @ApiModelProperty(value = "创建人姓名")
    private String createUserName;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 使用此角色的用户数
     */
    @ApiModelProperty(value = "使用此角色的用户数")
    private Integer userCount;
}
