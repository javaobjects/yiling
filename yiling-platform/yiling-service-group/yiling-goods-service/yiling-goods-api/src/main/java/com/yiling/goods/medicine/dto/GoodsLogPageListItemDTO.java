package com.yiling.goods.medicine.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 日志列表DTO
 * @author dexi.yao
 * @date 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsLogPageListItemDTO extends BaseDTO {

	private static final long serialVersionUID = 7086948797618732087L;

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
	 * 修改项 1-上下架 2-修改库存 3-修改价格
	 */
	private String modifyColumn;

	/**
	 * 原始值
	 */
	private String beforeValue;

	/**
	 * 修改以后的值
	 */
	private String afterValue;

	/**
	 * 创建人
	 */
	private Long createUser;

	/**
	 * 创建人名称
	 */
	private String oprUser;

	/**
	 * 操作时间
	 */
	private Date createTime;
}
