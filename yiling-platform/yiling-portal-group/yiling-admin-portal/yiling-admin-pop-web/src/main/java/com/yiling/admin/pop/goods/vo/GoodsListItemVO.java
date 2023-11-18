package com.yiling.admin.pop.goods.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class GoodsListItemVO extends BaseVO {

	/**
	 * 标准库ID
	 */
	@ApiModelProperty(value = "标准库ID", example = "1111")
	private Long standardId;

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
	 * 一级分类名称
	 */
	@ApiModelProperty(value = "一级分类名称", example = "11111")
	private String standardCategoryName1;

	/**
	 * 二级分类名称
	 */
	@ApiModelProperty(value = "二级分类名称", example = "2222")
	private String standardCategoryName2;

	/**
	 * 销售规格
	 */
	@ApiModelProperty(value = "销售规格", example = "1片")
	private String sellSpecifications;

    /**
     * 自己的销售规格
     */
    @ApiModelProperty(value = "自己公司销售规格", example = "1片")
    private String specifications;

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	@ApiModelProperty(value = "品状态：1上架 2下架 5待审核 6驳回（只有1是上架状态，其余全为下架）", example = "1")
	private Integer goodsStatus;

	/**
	 * 挂网价
	 */
	@ApiModelProperty(value = "挂网价", example = "1.11")
	private BigDecimal price;

    /**
     * 下架原因：1平台下架 2质管下架 3供应商下架
     */
    @ApiModelProperty(value = "下架原因：1平台下架 2质管下架 3供应商下架", example = "1")
    private Integer outReason;

	/**
	 * 商品图片值
	 */
	@ApiModelProperty(value = "商品图片值", example = "https://xxxx.xxxx.xxxx/goods.jpg")
	private String pic;

    /**
     * 销售包装
     */
    @ApiModelProperty(value = "销售包装")
    private List<GoodsSkuVO> goodsSkuList;

    @ApiModelProperty(value = "是否已经包含其它属性（协议）")
    private GoodsDisableVO goodsDisableVO;

}
