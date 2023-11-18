package com.yiling.b2b.admin.goods.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户分组定价 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPriceCustomerGroupVO extends BaseVO {

    /**
     * 供应商企业ID
     */
    @ApiModelProperty(value = "供应商企业ID")
    private Long eid;

    /**
     * 客户分组ID
     */
    @ApiModelProperty(value = "客户分组ID")
    private Long customerGroupId;

    /**
     * 客户分组名称
     */
    @ApiModelProperty(value = "客户分组名称")
    private String customerGroupName;

    /**
     * 客户数
     */
    @ApiModelProperty(value = "客户数")
    private Integer customerNum;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 定价规则：1-浮动点位 2-具体价格
     */
    @ApiModelProperty(value = "定价规则：1-浮动点位 2-具体价格")
    private Integer priceRule;

    /**
     * 浮动点位/价格
     */
    @ApiModelProperty(value = "浮动点位/价格")
    private BigDecimal priceValue;

}
