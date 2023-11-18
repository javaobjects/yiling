package com.yiling.user.agreement.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementGoodsDistributorInfoDTO extends BaseDTO {

	private static final long serialVersionUID = -5417249492995997028L;
	/**
	 * 配送商id
	 */
	private Long distributorId;

	/**
	 * 配送商名称
	 */
	private String distributorName;

	/**
	 * 商品id
	 */
	private Long goodsId;

	/**
	 * 协议类型 1-双方协议 2-三方协议
	 */
	private Integer type;

}
