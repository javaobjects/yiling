package com.yiling.f2b.admin.procrelation.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023/6/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TemplateGoodsVO extends BaseVO {

    /**
     * 专利类型 0-全部 1-非专利 2-专利
     */
    @ApiModelProperty("专利类型 0-全部 1-非专利 2-专利")
    private Integer isPatent;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String goodsName;

    /**
     * 售卖规格
     */
    @ApiModelProperty("售卖规格")
    private String sellSpecifications;

    /**
     * 批准文号
     */
    @ApiModelProperty("批准文号")
    private String licenseNo;

    /**
     * 商品优化折扣，单位为百分比
     */
    @ApiModelProperty("商品优化折扣，单位为百分比")
    private BigDecimal rebate;
}
