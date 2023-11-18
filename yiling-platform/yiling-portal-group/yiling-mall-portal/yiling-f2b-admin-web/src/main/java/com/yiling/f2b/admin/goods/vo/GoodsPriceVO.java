package com.yiling.f2b.admin.goods.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品定价 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPriceVO extends BaseVO {

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
     * 只展示上下架商品状态：1上架 2下架
     */
    @ApiModelProperty(value = "商品状态：1上架 2下架", example = "1")
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
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Long eid;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String ename;

    @ApiModelProperty(value = "分组定价区间", example = "1.1 ~ 2.2")
    private String customerGroupPrice;

    @ApiModelProperty(value = "客户定价区间", example = "1.1 ~ 2.2")
    private String customerPrice;

    @ApiModelProperty(value = "客户数：x(客户定价个数)/x(总客户数)", example = "15/20")
    private String customerPriceNum;

    @ApiModelProperty(value = "分组数：x(客户分组定价个数)/x(总客户分组数)", example = "15/20")
    private String customerGroupPriceNum;

    @ApiModelProperty(value = "商品图片")
    private String pic;
}
