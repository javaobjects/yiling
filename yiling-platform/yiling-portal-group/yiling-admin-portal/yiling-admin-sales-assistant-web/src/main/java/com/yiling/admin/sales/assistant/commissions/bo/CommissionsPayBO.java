package com.yiling.admin.sales.assistant.commissions.bo;

import java.math.BigDecimal;
import java.util.Objects;

import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-09-27
 */
@Data
public class CommissionsPayBO {

	/**
	 * 佣金明细id
	 */
	private Long id;

	/**
	 * 兑付金额
	 */
	private BigDecimal subAmount;


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CommissionsPayBO)) {
			return false;
		}
		CommissionsPayBO that = (CommissionsPayBO) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
