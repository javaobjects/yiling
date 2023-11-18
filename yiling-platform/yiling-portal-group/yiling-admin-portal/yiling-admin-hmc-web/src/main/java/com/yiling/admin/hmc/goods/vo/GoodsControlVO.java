package com.yiling.admin.hmc.goods.vo;

import java.math.BigDecimal;

import com.yiling.admin.hmc.insurance.vo.GoodsDisableVO;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保险药品
 */
@Data
public class GoodsControlVO extends BaseVO {

    /**
     * 商品保险id
     */
    private Long id;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String name;

    /**
     * 注册证号
     */
    @ApiModelProperty(value = "批准文号")
    private String licenseNo;

    /**
     * 标准库规格id
     */
    @ApiModelProperty(value = "标准库规格id")
    private Long sellSpecificationsId;
    /**
     * 标准库商品id
     */
    @ApiModelProperty(value = "标准库商品id")
    private Long standardId;
    /**
     * 商品市场价
     */
    @ApiModelProperty(value = "商品市场价")
    private BigDecimal marketPrice;
    /**
     * 参保价
     */
    @ApiModelProperty(value = "参保价")
    private BigDecimal insurancePrice;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String sellSpecifications;

    @ApiModelProperty(value = "1-开启 0-关闭")
    private Integer controlStatus;

    @ApiModelProperty(value = "是否已经包含其它属性（协议）")
    private GoodsDisableVO goodsDisableVO;
}
