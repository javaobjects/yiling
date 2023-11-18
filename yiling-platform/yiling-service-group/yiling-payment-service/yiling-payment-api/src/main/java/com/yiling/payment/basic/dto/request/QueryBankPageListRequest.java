package com.yiling.payment.basic.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBankPageListRequest extends QueryPageListRequest {

	/**
	 * 银行类型 1-总行 2-支行
	 */
	private Integer type;

	/**
	 * 名称(模糊）
	 */
	private String name;
}
