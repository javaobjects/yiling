package com.yiling.goods.medicine.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsLogRequest extends QueryPageListRequest {

	private static final long serialVersionUID = -4585938578458780662L;

	/**
	 * 商品ID
	 */
	private Long gid;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 批准文号
	 */
	private String licenseNo;

	/**
	 * 修改项
	 */
	private String modifyColumn;

	/**
	 * 操作人
	 */
	private List<Long> operUserList;

	/**
	 * 操作开始时间
	 */
	private Date startTime;

	/**
	 * 操作结束时间
	 */
	private Date endTime;

}
