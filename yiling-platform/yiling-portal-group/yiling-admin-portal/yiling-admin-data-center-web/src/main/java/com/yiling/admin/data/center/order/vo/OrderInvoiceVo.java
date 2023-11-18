package com.yiling.admin.data.center.order.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发票详情
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceVo extends BaseVO {

    @ApiModelProperty(value = "发票单号")
    private String invoiceNo;

    @ApiModelProperty(value = "发票申请ID")
    private Long applyId;
}
