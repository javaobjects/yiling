package com.yiling.b2b.admin.goods.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

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
@ApiModel(value = "b2b商品参数")
public class QueryGoodsPageListForm extends QueryPageListForm {

	/**
	 * 企业id
	 */
	@ApiModelProperty(value = "企业ID", example = "1")
	private Long eid;

    /**
     * 标准库ID
     */
    @ApiModelProperty(value = "标准库ID", example = "1")
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
	 * 标准库一级分类id
	 */
	@ApiModelProperty(value = "标准库一级分类id", example = "1")
	private Long standardCategoryId1;

	/**
	 * 标准库二级分类id
	 */
	@ApiModelProperty(value = "标准库二级分类id", example = "1")
	private Long standardCategoryId2;

	/**
	 * 销售规格
	 */
	@ApiModelProperty(value = "销售规格", example = "1片")
	private String sellSpecifications;

	/**
	 * 商品状态：1已上架 2未上架 3待设置
	 */
	@ApiModelProperty(value = "商品状态：1已上架 2未上架 3待设置", example = "4")
	private Integer goodsStatus;

    /**
     * 下架原因
     */
    @ApiModelProperty(value = "下架原因", example = "4")
    private Integer outReason;

    /**
     * 产品线：0 全部 1 pop 2 b2b
     */
    @ApiModelProperty(value = "产品线：0 全部 1 pop 2 b2b", example = "0")
    private Integer goodsLine;



}
