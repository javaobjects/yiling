package com.yiling.f2b.admin.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderFlowForm extends QueryPageListForm {

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;

    @ApiModelProperty(value = "商品批准文号")
    private String licenseNo;

    @ApiModelProperty(value = "商品规格")
    private String sellSpecifications;

    @ApiModelProperty(value = "采购商渠道")
    private Long buyerChannelId;

    @ApiModelProperty(value = "下单开始时间")
    private Date startCreateTime;

    @ApiModelProperty(value = "下单结束时间")
    private Date endCreateTime;

    @ApiModelProperty(value = "发货开始时间")
    private Date startDeliveryTime;

    @ApiModelProperty(value = "发货结束时间")
    private Date endDeliveryTime;

    @ApiModelProperty(value = "供应商名称")
    private String sellerEname;

    public Date getStartCreateTime() {
        if (startCreateTime != null) {
            return DateUtil.beginOfDay(startCreateTime);
        }
        return null;
    }

    public Date getEndCreateTime() {
        if (endCreateTime != null) {
            return DateUtil.endOfDay(endCreateTime);
        }
        return null;
    }

    public Date getStartDeliveryTime() {
        if (startDeliveryTime != null) {
            return DateUtil.beginOfDay(startDeliveryTime);
        }
        return null;
    }

    public Date getEndDeliveryTime() {
        if (endDeliveryTime != null) {
            return DateUtil.endOfDay(endDeliveryTime);
        }
        return null;
    }
}
