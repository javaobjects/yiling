package com.yiling.user.agreement.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 三方协议-企业分页dto
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ThirdAgreementEntPageListItemDTO extends BaseDTO {

	private static final long serialVersionUID = 5692083653289253853L;

	/**
	 * 企业id
	 */
	private Long eid;

	/**
	 * 协议角色 1-乙方 2-丙方
	 */
	private Integer role;

}
