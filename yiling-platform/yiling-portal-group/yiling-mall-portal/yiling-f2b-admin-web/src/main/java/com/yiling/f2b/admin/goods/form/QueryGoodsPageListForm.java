package com.yiling.f2b.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
public class QueryGoodsPageListForm extends QueryPageListForm {

    /**
     * 商品Id
     */
    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

	/**
	 * 注册证号
	 */
	@ApiModelProperty(value = "批准文号", example = "Z109090")
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
     * 标准库一级分类id
     */
    @ApiModelProperty(value = "标准库一级分类id", example = "11111")
    private Long standardCategoryId1;

    /**
     * 标准库二级分类id
     */
    @ApiModelProperty(value = "标准库二级分类id", example = "2222")
    private Long standardCategoryId2;

	/**
     * erp编码
     */
    @ApiModelProperty(value = "erp编码", example = "2222")
    private String inSn;

    /**
     * erp内码
     */
    @ApiModelProperty(value = "erp内码", example = "2222")
    private String sn;

	/**
	 * 商品状态：1上架 2下架 5待审核 6驳回
	 */
	@ApiModelProperty(value = "品状态：1上架 2下架 5待审核 6驳回", example = "1")
	private Integer goodsStatus;

	/**
	 * 下架原因
	 */
	@ApiModelProperty(value = "下架原因", example = "1")
	private Integer outReason;
}
