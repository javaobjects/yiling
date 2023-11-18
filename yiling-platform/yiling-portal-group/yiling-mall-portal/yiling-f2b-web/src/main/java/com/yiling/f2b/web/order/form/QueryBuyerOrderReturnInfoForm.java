package com.yiling.f2b.web.order.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBuyerOrderReturnInfoForm extends QueryPageListForm {
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号", required = true)
    private String orderNo;
    /**
     * 退货单号
     */
    @ApiModelProperty(value = "退货单号", required = true)
    private String orderReturnNo;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称", required = true)
    private String sellerEname;

    /**
     * 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
     */
    @ApiModelProperty(value = "1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单", required = true)
    private Integer returnType;

    /**
     * 订单状态 1-待审核 2-审核通过 3-审核驳回
     */
    @ApiModelProperty(value = "订单状态 1-待审核 2-审核通过 3-审核驳回", required = true)
    private Integer returnStatus;

    /**
     * 订单Id
     */
    @ApiModelProperty(value = "订单Id")
    private Long orderId;

}
