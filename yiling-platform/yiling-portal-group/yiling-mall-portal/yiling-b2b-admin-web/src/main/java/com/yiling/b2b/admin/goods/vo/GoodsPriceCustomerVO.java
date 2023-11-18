package com.yiling.b2b.admin.goods.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户定价 VO
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsPriceCustomerVO extends BaseVO {

    /**
     * 供应商企业ID
     */
    @ApiModelProperty(value = "供应商企业ID")
    private Long eid;

    /**
     * 客户企业ID
     */
    @ApiModelProperty(value = "客户企业ID")
    private Long customerEid;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;

    /**
     * 生产地址
     */
    @ApiModelProperty(value = "生产厂家", example = "以岭")
    private String address;

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
