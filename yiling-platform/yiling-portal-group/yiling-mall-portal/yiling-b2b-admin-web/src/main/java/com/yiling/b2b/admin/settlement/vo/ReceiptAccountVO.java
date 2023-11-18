package com.yiling.b2b.admin.settlement.vo;

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
	 * 开户许可证
	 */
	@ApiModelProperty(value = "开户许可证")
	private String accountOpeningPermitUrl;

	/**
	 * 企业收款账户信息
	 */
	@ApiModelProperty(value = "企业收款账户信息")
	private AccountVo accountVo;

	@Data
	public static class  AccountVo{
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
		 * 总行id
		 */
		@ApiModelProperty(value = "总行id")
		private Long bankId;

		/**
		 * 支行id
		 */
		@ApiModelProperty(value = "支行id")
		private Long branchBankId;

		/**
		 * 总行编码
		 */
		@ApiModelProperty(value = "总行编码")
		private String bankCode;

		/**
		 * 支行编码
		 */
		@ApiModelProperty(value = "支行编码")
		private String branchCode;

		/**
		 * 省份code
		 */
		@ApiModelProperty(value = "省份code")
		private String provinceCode;

		/**
		 * 省份name
		 */
		@ApiModelProperty(value = "省份name")
		private String provinceName;

		/**
		 * 市code
		 */
		@ApiModelProperty(value = "市code")
		private String cityCode;

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
		@ApiModelProperty(value = "其他证照key")
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



}
