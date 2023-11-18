package com.yiling.admin.data.center.report.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * B2B订单报表统计
 * @author: wei.wang
 * @date: 2022-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryB2BOrderCountForm extends BaseForm {

    /**
     *供应商EID
     */
    @ApiModelProperty("供应商EID")
    private Long sellerEid;

    /**
     * 开始时间
     */
    @NotNull
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款 4-在线支付")
    private Integer paymentMethod;

    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "所属省份编码")
    private String provinceCode;

    /**
     *所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    @ApiModelProperty("订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台")
    private Integer orderSource;

    /**
     * 商品id标签
     */
    @ApiModelProperty("商品id标签")
    private Long standardGoodsTagId;

    /**
     * 下单时是否会员 1-非会员 2-是会员
     */
    @ApiModelProperty("下单时是否会员 1-非会员 2-是会员")
    private Integer vipFlag;
}
