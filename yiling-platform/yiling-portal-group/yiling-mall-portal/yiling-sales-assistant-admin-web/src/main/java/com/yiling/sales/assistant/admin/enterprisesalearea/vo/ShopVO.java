package com.yiling.sales.assistant.admin.enterprisesalearea.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-店铺设置VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class ShopVO extends BaseVO {

    /**
     * 店铺企业ID
     */
    @ApiModelProperty("店铺企业ID")
    private Long shopEid;

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;

    /**
     * 店铺logo
     */
    @ApiModelProperty("店铺logo")
    private String shopLogo;

    /**
     * 店铺简介
     */
    @ApiModelProperty("店铺简介")
    private String shopDesc;

    /**
     * 起配金额
     */
    @ApiModelProperty("起配金额")
    private BigDecimal startAmount;

    /**
     * 支付方式（字典payment_method）
     */
    @ApiModelProperty("支付方式（字典payment_method）")
    private List<PaymentMethodVO> paymentMethodList;

    /**
     * 店铺区域Json
     */
    @ApiModelProperty("店铺区域Json")
    private String areaJsonString;

    /**
     * 销售区域描述
     */
    @ApiModelProperty("销售区域描述")
    private String description;

    @Data
    public static class PaymentMethodVO {

        @ApiModelProperty("支付方式ID")
        private Long id;

        @ApiModelProperty("支付方式名称")
        private String name;

        @ApiModelProperty("支付方式备注")
        private String remark;

        @ApiModelProperty("是否选中")
        private Boolean checked;

        @ApiModelProperty("是否禁用")
        private Boolean disabled;
    }
}
