package com.yiling.f2b.admin.goods.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 日志列表VO
 * @author dexi.yao
 * @date 2021-06-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsLogPageListItemVO extends BaseVO {

	/**
	 * 商品ID
	 */
	@ApiModelProperty(value = "商品ID")
	private Long gid;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String name;

	/**
	 * 批准文号
	 */
	@ApiModelProperty(value = "批准文号")
	private String licenseNo;

	/**
	 * 修改项 1-上下架 2-修改库存 3-修改价格
	 */
	@ApiModelProperty(value = "修改项 1-上下架 2-修改库存 3-修改价格")
	private String modifyColumn;

	/**
	 * 修改以后的值
	 */
	@ApiModelProperty(value = "修改以后的值")
	private String beforeValue;

	/**
	 * 修改以后的值
	 */
	@ApiModelProperty(value = "修改以后的值")
	private String afterValue;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private String oprUser;

	/**
	 * 操作时间
	 */
	@ApiModelProperty(value = "操作时间")
	private Date createTime;
}
