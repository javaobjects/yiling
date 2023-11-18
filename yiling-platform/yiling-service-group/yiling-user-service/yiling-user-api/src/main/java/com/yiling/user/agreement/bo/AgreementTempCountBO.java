package com.yiling.user.agreement.bo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 协议下的子协议个数BO
 * @author dexi.yao
 * @date 2021-06-11
 */
@Data
@Accessors(chain = true)
public class AgreementTempCountBO implements Serializable {
    private static final long serialVersionUID = -9044217574008714L;
	/**
	 * 子协议数量
	 */
	private Integer count;

	/**
	 * 主协议id
	 */
	private Long parentId;
}
