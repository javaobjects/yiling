package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发票信息
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceApplyAllInfoVO extends BaseVO {

    /**
     *状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    @ApiModelProperty(value = "开票状态：1-待申请 2-部分申请  4-部分开票 3-已开票 5-已作废 6-不开票")
    private Integer status;

    @ApiModelProperty(value = "已开发票张数")
    private Integer invoiceNumber;

    @ApiModelProperty(value = "发票详情")
    private List<OrderInvoiceVo> orderInvoiceInfo;

    @ApiModelProperty(value = "已开票金额")
    private BigDecimal invoiceFinishAmount;
}
