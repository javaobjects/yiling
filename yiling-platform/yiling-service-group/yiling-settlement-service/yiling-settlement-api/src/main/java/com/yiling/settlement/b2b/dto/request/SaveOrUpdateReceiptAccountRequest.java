package com.yiling.settlement.b2b.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

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
public class SaveOrUpdateReceiptAccountRequest extends BaseRequest {

	/**
	 * 企业收款账户信息id
	 */
	private Long receiptAccountId;

	/**
	 * eid
	 */
	private Long eid;

	/**
	 * 企业收款账户名称
	 */
	private String name;

	/**
	 * 银行账户
	 */
	private String account;

	/**
	 * 开户行名称
	 */
	private String bankName;

	/**
	 * 支行名称
	 */
	private String subBankName;

	/**
	 * 总行id
	 */
	private Long bankId;

	/**
	 * 支行id
	 */
	private Long branchBankId;

	/**
	 * 总行编码
	 */
	private String bankCode;

	/**
	 * 支行行号
	 */
	private String branchNum;

	/**
	 * 省份code
	 */
	private String provinceCode;

	/**
	 * 省份name
	 */
	private String provinceName;

	/**
	 * 市code
	 */
	private String cityCode;

	/**
	 * 市name
	 */
	private String cityName;

	/**
	 * 其他证照
	 */
	private String licence;

}
