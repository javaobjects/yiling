package com.yiling.user.agreement.bo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 企业的三方协议信息
 * @author dexi.yao
 * @date 2021-06-07
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EntThirdAgreementInfoBO implements Serializable {
    private static final long serialVersionUID = -9044217574008714L;
	/**
	 * 年度协议个数
	 */
	private Integer yearAgreementCount;

	/**
	 * 临时协议个数
	 */
	private Integer tempAgreementCount;

}
