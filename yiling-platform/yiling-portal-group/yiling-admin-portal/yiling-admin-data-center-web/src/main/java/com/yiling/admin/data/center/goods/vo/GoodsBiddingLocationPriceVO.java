package com.yiling.admin.data.center.goods.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "招标挂网价分页vo")
public class GoodsBiddingLocationPriceVO extends BaseVO {

	/**
	 * 商品ID
	 */
	@ApiModelProperty(value = "商品ID", example = "1")
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
    @ApiModelProperty(value = "销售规格", example = "1片")
    private String specifications;

	/**
	 * 商品图片值
	 */
	@ApiModelProperty(value = "商品图片值", example = "https://xxxx.xxxx.xxxx/goods.jpg")
	private String pic;

	/**
	 * 设置招标价的省份数量
	 */
	@ApiModelProperty(value = "设置招标价的省份数量", example = "1")
	private Integer locationCount;

	/**
	 * 地区价格对应列表
	 */
	@ApiModelProperty(value = "设置招标价的省份数量", example = "1")
	private List<LocationPrice> LocationPriceList;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static  class LocationPrice{

		/**
		 * 地区编码
		 */
		@ApiModelProperty(value = "地区编码", example = "1000001")
		private String locationCode;

		/**
		 * 地区名称
		 */
		@ApiModelProperty(value = "地区名称", example = "北京")
		private String locationName;

		/**
		 * 招标价格
		 */
		@ApiModelProperty(value = "招标价格", example = "1.11")
		private BigDecimal price;
	}
}
