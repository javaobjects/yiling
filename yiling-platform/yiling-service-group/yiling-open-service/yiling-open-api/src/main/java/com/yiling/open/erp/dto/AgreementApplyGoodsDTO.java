package com.yiling.open.erp.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利申请商品表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-08-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementApplyGoodsDTO extends BaseDTO {


	/**
	 * code
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

}
