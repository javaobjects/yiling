package com.yiling.open.erp.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ValidAgreementApplyImportDTO extends BaseDTO {

	/**
	 * 异常信息
	 */
	private String msg;

	/**
	 *
	 */
	private String result;

	/**
	 * 如果200就不用管了，说明没问题
	 */
	private String code;

	/**
	 *
	 */
	private String resultCount;
}
