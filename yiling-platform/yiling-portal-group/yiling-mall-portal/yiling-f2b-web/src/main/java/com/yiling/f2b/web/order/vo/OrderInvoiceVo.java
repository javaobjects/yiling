package com.yiling.f2b.web.order.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
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
