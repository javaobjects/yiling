package com.yiling.admin.data.center.enterprise.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-10-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("收款账户信息")
public class ReceiptAccountVO extends BaseVO {

	/**
	 * 企业名称
	 */
	@ApiModelProperty(value = "企业名称")
	private String entName;

	/**
	 * 社会信用统一代码
	 */
	@ApiModelProperty(value = "社会信用统一代码")
	private String licenseNumber;

	/**
	 * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
	 */
	@ApiModelProperty(value = "企业类型")
	private Integer type;

	/**
	 * 企业收款账户名称
	 */
	@ApiModelProperty(value = "企业收款账户名称")
	private String name;

	/**
	 * 银行账户
	 */
	@ApiModelProperty(value = "银行账户")
	private String account;

	/**
	 * 开户行名称
	 */
	@ApiModelProperty(value = "开户行名称")
	private String bankName;

	/**
	 * 支行名称
	 */
	@ApiModelProperty(value = "支行名称")
	private String subBankName;

	/**
	 * 省份name
	 */
	@ApiModelProperty(value = "省份name")
	private String provinceName;

	/**
	 * 市name
	 */
	@ApiModelProperty(value = "市name")
	private String cityName;

	/**
	 * 账户状态 1-待审核 2-审核成功 3-审核失败
	 */
	@ApiModelProperty(value = "账户状态 1-待审核 2-审核成功 3-审核失败")
	private Integer status;

	/**
	 * 审核描述
	 */
	@ApiModelProperty(value = "审核描述")
	private String auditRemark;

	/**
	 * 其他证照
	 */
	@ApiModelProperty(value = "其他证照", hidden = true)
	@JsonIgnore
	private String licence;

	/**
	 * 其他证照
	 */
	@ApiModelProperty(value = "其他证照")
	private String licenceUrl;

	/**
	 * 开户许可证
	 */
	@ApiModelProperty(value = "开户许可证")
	private String accountOpeningPermitUrl;


}
