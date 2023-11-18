package com.yiling.open.erp.bo;

import lombok.Data;

/**
 * 查询冲红系统bo
 * @author dexi.yao
 * @date 2021-08-12
 */
@Data
public class QueryRedFlushBO {

	/**
	 * key
	 */
	private String keyName;

	/**
	 * value
	 */
	private String keyValue;

	/**
	 * 状态
	 */
	private String status;
}
