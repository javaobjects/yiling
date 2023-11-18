package com.yiling.f2b.admin.goods.form;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateGoodsForm extends BaseForm {

	/**
	 * 商品库ID
	 */
	@ApiModelProperty(value = "商品库ID", example = "1111")
	private Long id;

    @ApiModelProperty(value = "商品库ID", example = "1111")
    private Long sellSpecificationsId;

    @ApiModelProperty(value = "所属公司ID")
    private Long eid;

    @ApiModelProperty(value = "商品库ID", example = "1111")
    private Long  standardId;

    /**
     * 标准库一级分类名称
     */
    @ApiModelProperty(value = "标准库一级分类名称", example = "0")
    private Long standardCategoryId1;

    /**
     * 标准库二级分类名称
     */
    @ApiModelProperty(value = "标准库二级分类名称", example = "0")
    private Long standardCategoryId2;

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
     * 生产厂家地址
     */
    @ApiModelProperty(value = "生产厂家地址", example = "石家庄")
    private String manufacturerAddress;

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
     * 规格单位
     */
    @ApiModelProperty(value = "规格单位", example = "盒")
    private String unit;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码", example = "112312312")
    private String sn;

    /**
     * 商品内码
     */
    @ApiModelProperty(value = "商品内码", example = "1213123123")
    private String inSn;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量", example = "10")
    @Range(message = "库存数量为 {min} 到 {max} 之间", min = 0, max = 999999)
    private Long qty;

    /**
     * 中包装
     */
    @ApiModelProperty(value = "中包装", example = "10")
    private Integer  middlePackage;


    /**
     * 大包装
     */
    @ApiModelProperty(value = "大包装", example = "10")
    private Integer bigPackage;

    /**
     * 是否拆包销售：1可拆0不可拆
     */
    @ApiModelProperty(value = "是否拆包销售：1可拆0不可拆(不可编辑)", example = "1")
    private Integer canSplit;


    @ApiModelProperty(value = "专利标识: 1-非专利 2-专利", example = "1")
    private Integer isPatent;


}
