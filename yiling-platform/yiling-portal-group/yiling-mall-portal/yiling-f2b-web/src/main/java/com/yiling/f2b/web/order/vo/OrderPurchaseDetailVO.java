package com.yiling.f2b.web.order.vo;

import java.util.List;

import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 采购订单详情列表VO
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderPurchaseDetailVO extends OrderSellerAndPurchaseDetailCommonVO {


    @ApiModelProperty(value = "供应商信息")
    private EnterpriseInfoVO enterpriseInfo;

    @ApiModelProperty(value = "是否允许退货,0 允许，1不允许")
    private Integer isAllowReturn = 0;

    @ApiModelProperty(value = "不允许退货的原因")
    private String refuseReturnReason;
}
