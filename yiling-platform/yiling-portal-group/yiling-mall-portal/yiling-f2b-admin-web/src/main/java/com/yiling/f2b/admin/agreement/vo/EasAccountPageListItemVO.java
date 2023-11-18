package com.yiling.f2b.admin.agreement.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EasAccountPageListItemVO extends BaseVO {

	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "企业id")
	private Long customerEid;

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String easName;

	/**
	 * 企业账号
	 */
	@ApiModelProperty(value = "企业编码")
	private String easCode;

	/**
	 * 联系人
	 */
	@ApiModelProperty(value = "联系人")
	private String contactor;

	/**
	 * 联系人电话
	 */
	@ApiModelProperty(value = "联系人电话")
	private String contactorPhone;

	/**
	 * 渠道ID
	 */
	@ApiModelProperty(value = "渠道ID")
	private Long channelId;

	/**
	 * 企业地址
	 */
	@ApiModelProperty(value = "企业地址")
	private String address;

	/**
	 * 是否有兑付一级商 1-有 2-没有
	 */
	@ApiModelProperty(value = "是否有兑付一级商 true-有 false-没有")
	private Boolean isFirst;


}
