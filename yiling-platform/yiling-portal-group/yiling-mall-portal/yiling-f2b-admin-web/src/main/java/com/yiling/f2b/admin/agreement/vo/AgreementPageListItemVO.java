package com.yiling.f2b.admin.agreement.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("协议列表VO")
public class AgreementPageListItemVO extends BaseVO {

	/**
	 * 乙方id
	 */
	@ApiModelProperty(value = "乙方id")
	private Long secondEid;

	/**
	 * 协议名称
	 */
	@ApiModelProperty(value = "协议名称")
	private String name;

	/**
	 * 协议描述
	 */
	@ApiModelProperty(value = "协议描述")
	private String content;

	/**
	 * 主体名称
	 */
	@ApiModelProperty(value = "主体名称")
	private String  ename;

	/**
	 * 补充协议个数
	 */
	@ApiModelProperty(value = "补充协议个数，，只有当前列表为年度协议时会有该字段")
	private Integer tempCount;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	/**
	 * 协议状态：1-进行中 2-未开始 3-已停用 4-已过期
	 */
	@ApiModelProperty(value = "协议状态：1-进行中 2-未开始 3-已停用 4-已过期")
	private Integer   agreementStatus;

	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称")
	private String createUserName;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人",hidden = true)
	@JsonIgnore
	private Long createUser;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人",hidden = true)
	@JsonIgnore
	private Long updateUser;

	/**
	 * 修改人名称
	 */
	@ApiModelProperty(value = "修改人名称")
	private String updateUserName;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private Date updateTime;

}
