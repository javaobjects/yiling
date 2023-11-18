package com.yiling.admin.data.center.goods.vo;

import java.math.BigDecimal;

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

    @ApiModelProperty(value = "自己公司销售规格", example = "1片")
    private String specifications;

	/**
	 * 品状态：4审核通过 5待审核 6驳回
	 */
	@ApiModelProperty(value = "品状态：4审核通过 5待审核 6驳回", example = "1")
	private Integer auditStatus;

	/**
	 * 挂网价
	 */
	@ApiModelProperty(value = "挂网价", example = "1.11")
	private BigDecimal price;

	/**
	 * 库存数量
	 */
	@ApiModelProperty(value = "库存数量", example = "10")
	private Long qty;

    @ApiModelProperty(value = "冻结数量", example = "10")
    private Long frozenQty;

    @ApiModelProperty(value = "实际库存")
    private Long realQty;
	/**
	 * 中包装
	 */
	@ApiModelProperty(value = "中包装", example = "100")
	private Integer middlePackage;

	/**
	 * 大包装
	 */
	@ApiModelProperty(value = "大包装", example = "10")
	private Integer bigPackage;

	/**
	 * 是否拆包销售：1可拆0不可拆
	 */
	@ApiModelProperty(value = "是否拆包销售：1可拆,0不可拆", example = "1")
	private Integer canSplit;

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

    @ApiModelProperty(value = "产品线", example = "pop,b2b")
    private String goodsLineDesc;

    @ApiModelProperty(value = "是否已经包含其它属性（协议）")
    private GoodsDisableVO goodsDisableVO;

}
