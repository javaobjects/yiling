package com.yiling.f2b.admin.goods.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
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
public class QueryGoodsLogForm extends QueryPageListForm {

	/**
	 * 商品ID
	 */
	@ApiModelProperty(value = "商品ID", example = "1")
	private Long gid;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称", example = "商品名称")
	private String name;

	/**
	 * 批准文号
	 */
	@ApiModelProperty(value = "批准文号", example = "NO.199191919")
	private String licenseNo;

	/**
	 * 修改项 1-上下架 2-修改库存 3-修改价格
	 */
	@ApiModelProperty(value = "修改项 1-上下架 2-修改库存 3-修改价格", example = "1")
	private String modifyColumn;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private String operUser;

	/**
	 * 操作开始时间
	 */
	@ApiModelProperty(value = "操作开始时间")
	private Date startTime;

	/**
	 * 操作结束时间
	 */
	@ApiModelProperty(value = "操作结束时间")
	private Date endTime;

}
