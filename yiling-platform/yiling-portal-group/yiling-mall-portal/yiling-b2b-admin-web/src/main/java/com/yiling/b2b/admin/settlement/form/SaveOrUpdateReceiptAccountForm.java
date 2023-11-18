package com.yiling.b2b.admin.settlement.form;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

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
public class SaveOrUpdateReceiptAccountForm extends BaseForm {

	/**
	 * 企业收款账户信息id
	 */
	@ApiModelProperty("企业收款账户信息id---修改账户信息时必传")
	private Long receiptAccountId;

	/**
	 * 企业收款账户名称
	 */
	@ApiModelProperty("企业收款账户名称")
	@Length(max = 50)
	private String name;

	/**
	 * 银行账户
	 */
	@ApiModelProperty("银行账户")
	@Length(max = 30)
	private String account;


	/**
	 * 总行id
	 */
	@ApiModelProperty("总行id")
	private Long bankId;

	/**
	 * 支行id
	 */
	@ApiModelProperty("支行id")
	private Long branchBankId;

	/**
	 * 省份code
	 */
	@ApiModelProperty("省份code")
	private String provinceCode;

	/**
	 * 省份name
	 */
	@ApiModelProperty("省份name")
	private String provinceName;

	/**
	 * 市code
	 */
	@ApiModelProperty("市code")
	private String cityCode;

	/**
	 * 市name
	 */
	@ApiModelProperty("市name")
	private String cityName;

	/**
	 * 其他证照
	 */
	@ApiModelProperty("其他证照")
	@Length(max = 200)
	private String licenceFile;
}
