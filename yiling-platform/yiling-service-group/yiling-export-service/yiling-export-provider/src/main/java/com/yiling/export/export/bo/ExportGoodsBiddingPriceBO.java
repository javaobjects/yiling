package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 导入招标挂网价 Form
 *
 * @author: dexi.yao
 * @date: 2021/07/08
 */
@Data
public class ExportGoodsBiddingPriceBO {


	/**
	 *商品ID
	 */
	private Long goodsId;


	/**
	 * 商品名称
	 */
	private String goodsName;


	/**
	 *批准文号
	 */
	private String licenseNo;


	/**
	 *生产厂家
	 */
	private String manufacturer;


	/**
	 *北京市
	 */
	private BigDecimal location_110000;


	/**
	 *天津市
	 */
	private BigDecimal location_120000;


	/**
	 *河北省
	 */
	private BigDecimal location_130000;


	/**
	 *山西省
	 */
	private BigDecimal location_140000;


	/**
	 *内蒙古自治区
	 */
	private BigDecimal location_150000;


	/**
	 *辽宁省
	 */
	private BigDecimal location_210000;


	/**
	 *吉林省
	 */
	private BigDecimal location_220000;


	/**
	 *黑龙江省
	 */
	private BigDecimal location_230000;


	/**
	 *上海市
	 */
	private BigDecimal location_310000;


	/**
	 *江苏省
	 */
	private BigDecimal location_320000;


	/**
	 *浙江省
	 */
	private BigDecimal location_330000;


	/**
	 *安徽省
	 */
	private BigDecimal location_340000;


	/**
	 *福建省
	 */
	private BigDecimal location_350000;


	/**
	 *江西省
	 */
	private BigDecimal location_360000;


	/**
	 *山东省
	 */
	private BigDecimal location_370000;


	/**
	 *河南省
	 */
	private BigDecimal location_410000;


	/**
	 *湖北省
	 */
	private BigDecimal location_420000;


	/**
	 *湖南省
	 */
	private BigDecimal location_430000;


	/**
	 *广东省
	 */
	private BigDecimal location_440000;


	/**
	 *广西壮族自治区
	 */
	private BigDecimal location_450000;


	/**
	 *海南省
	 */
	private BigDecimal location_460000;


	/**
	 *重庆市
	 */
	private BigDecimal location_500000;


	/**
	 *四川省
	 */
	private BigDecimal location_510000;


	/**
	 *贵州省
	 */
	private BigDecimal location_520000;


	/**
	 *云南省
	 */
	private BigDecimal location_530000;


	/**
	 *西藏自治区
	 */
	private BigDecimal location_540000;


	/**
	 *陕西省
	 */
	private BigDecimal location_610000;


	/**
	 *甘肃省
	 */
	private BigDecimal location_620000;


	/**
	 *青海省
	 */
	private BigDecimal location_630000;


	/**
	 *宁夏回族自治区
	 */
	private BigDecimal location_640000;


	/**
	 *新疆维吾尔自治区
	 */
	private BigDecimal location_650000;


	/**
	 *台湾省
	 */
	private BigDecimal location_710000;


	/**
	 *香港特别行政区
	 */
	private BigDecimal location_810000;


	/**
	 *澳门特别行政区
	 */
	private BigDecimal location_820000;
}
