package com.yiling.admin.data.center.order.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退货单明细批次信息返回给前端的实体数据
 *
 * @author: yong.zhang
 * @date: 2022/2/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReturnBatchVO extends BaseVO {

    @ApiModelProperty(value = "退货单ID")
    private Long returnId;

    @ApiModelProperty(value = "订单明细ID")
    private Long detailId;

    @ApiModelProperty(value = "批次号")
    private String batchNo;

    @ApiModelProperty(value = "有效期")
    private Date expiryDate;

    @ApiModelProperty(value = "退货数量")
    private Integer returnQuantity;

    @ApiModelProperty(value = "备注")
    private String remark;
}
