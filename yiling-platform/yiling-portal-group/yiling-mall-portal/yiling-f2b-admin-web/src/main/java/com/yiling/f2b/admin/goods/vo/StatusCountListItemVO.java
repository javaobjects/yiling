package com.yiling.f2b.admin.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StatusCountListItemVO extends BaseVO {

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	@ApiModelProperty(value = "品状态：1上架 2下架 5待审核 6驳回", example = "1")
	private Integer goodsStatus;

	/**
	 * 对应数量
	 */
	@ApiModelProperty(value = "对应数量", example = "10")
	private Integer count;
}
