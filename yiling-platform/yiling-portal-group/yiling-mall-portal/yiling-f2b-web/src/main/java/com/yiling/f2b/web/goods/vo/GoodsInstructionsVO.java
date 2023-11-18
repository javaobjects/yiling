package com.yiling.f2b.web.goods.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsInstructionsVO extends BaseVO {

	/**
	 * 药品成分
	 */
	@ApiModelProperty(value = "药品成分")
	private String drugDetails;

	/**
	 * 药品性状
	 */
	@ApiModelProperty(value = "药品性状")
	private String drugProperties;

	/**
	 * 适应症
	 */
	@ApiModelProperty(value = "适应症")
	private String indications;

	/**
	 * 用法与用量
	 */
	@ApiModelProperty(value = "用法与用量")
	private String usageDosage;

	/**
	 * 不良反应
	 */
	@ApiModelProperty(value = "不良反应")
	private String adverseEvents;

	/**
	 * 禁忌症
	 */
	@ApiModelProperty(value = "禁忌症")
	private String contraindication;

	/**
	 * 注意事项
	 */
	@ApiModelProperty(value = "注意事项")
	private String noteEvents;

	/**
	 * 药物相互作用
	 */
	@ApiModelProperty(value = "药物相互作用")
	private String interreaction;

	/**
	 * 存储条件
	 */
	@ApiModelProperty(value = "存储条件")
	private String storageConditions;

	/**
	 * 保质期
	 */
	@ApiModelProperty(value = "保质期")
	private String shelfLife;

	/**
	 * 执行标准
	 */
	@ApiModelProperty(value = "执行标准")
	private String executiveStandard;

	/**
	 * 净含量
	 */
	@ApiModelProperty(value = "净含量")
	private String netContent;

	/**
	 * 原产地
	 */
	@ApiModelProperty(value = "原产地")
	private String sourceArea;

	/**
	 * 保质期
	 */
	@ApiModelProperty(value = "保质期")
	private String expirationDate;

	/**
	 * 包装清单
	 */
	@ApiModelProperty(value = "包装清单")
	private String packingList;

	/**
	 * 储藏
	 */
	@ApiModelProperty(value = "储藏")
	private String store;

	/**
	 * 性味
	 */
	@ApiModelProperty(value = "性味")
	private String propertyFlavor;

	/**
	 * 功效
	 */
	@ApiModelProperty(value = "性味")
	private String effect;

	/**
	 * 灭菌类别
	 */
	@ApiModelProperty(value = "灭菌类别")
	private String sterilizationCategory;

	/**
	 * 原料
	 */
	@ApiModelProperty(value = "原料")
	private String rawMaterial;

	/**
	 * 辅料
	 */
	@ApiModelProperty(value = "辅料")
	private String ingredients;

	/**
	 * 适宜人群
	 */
	@ApiModelProperty(value = "适宜人群")
	private String suitablePeople;

	/**
	 * 不适宜人群
	 */
	@ApiModelProperty(value = "不适宜人群")
	private String unsuitablePeople;

	/**
	 * 保健功能
	 */
	@ApiModelProperty(value = "保健功能")
	private String healthcareFunction;


	/**
	 * 致敏源信息
	 */
	@ApiModelProperty(value = "致敏源信息")
	private String allergens;
}
