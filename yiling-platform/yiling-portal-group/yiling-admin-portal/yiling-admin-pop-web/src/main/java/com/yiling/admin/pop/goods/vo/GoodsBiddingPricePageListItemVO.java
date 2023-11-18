package com.yiling.admin.pop.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 招标挂网价分页vo
 * @author dexi.yao
 * @date 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "招标挂网价分页vo")
public class GoodsBiddingPricePageListItemVO extends BaseVO {


	/**
	 * 商品ID
	 */
	@ApiModelProperty(value = "商品ID", example = "12345")
	private Long goodsId;

	/**
	 * 注册证号
	 */
	@ApiModelProperty(value = "注册证号", example = "Z109090")
	private String licenseNo;

	/**
	 * 生产厂家
	 */
	@ApiModelProperty(value = "生产厂家", example = "以岭")
	private String manufacturer;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称", example = "莲花")
	private String name;

	/**
	 * 销售规格
	 */
	@ApiModelProperty(value = "销售规格", example = "1片")
	private String sellSpecifications;

    /**
     * 销售规格
     */
    @ApiModelProperty(value = "自己的销售规格", example = "1片")
    private String specifications;

	/**
	 * 设置招标价的省份数量
	 */
	@ApiModelProperty(value = "设置招标价的省份数量", example = "1")
	private Integer locationCount;

	public Long getGoodsId() {
		return this.getId();
	}
}
