package com.yiling.admin.b2b.common.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询退款单
 *
 * @author: yong.zhang
 * @date: 2021/11/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "供应商名称")
    private String sellerName;

    @ApiModelProperty(value = "采购商名称")
    private String buyerName;

    @ApiModelProperty(value = "退款状态0-全部 1-待退款 2-退款中 3-退款成功 4-退款失败")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款类型0-全部 1-订单取消退款 2-采购退货退款 3-商家驳回 4-会员退款")
    private Integer refundType;

    @ApiModelProperty(value = "创建开始时间")
    private Date createStartTime;

    @ApiModelProperty(value = "创建截止时间")
    private Date createStopTime;
}
