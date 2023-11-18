package com.yiling.admin.data.center.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:wei.wang
 * @date:2022/1/21
 */
@Data
public class QueryOrderNumberCountForm extends BaseForm {
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
     * 开始下单时间
     */
    @ApiModelProperty("开始下单时间")
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    @ApiModelProperty("结束下单时间")
    private Date endCreateTime;

    /**
     * 发货开始时间
     */
    @ApiModelProperty("发货开始时间")
    private Date  startDeliveryTime;

    /**
     * 发货结束时间
     */
    @ApiModelProperty("发货结束时间")
    private Date  endDeliveryTime;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    @ApiModelProperty("订单类型：1-POP订单,2-B2B订单")
    private Integer orderType;

}
