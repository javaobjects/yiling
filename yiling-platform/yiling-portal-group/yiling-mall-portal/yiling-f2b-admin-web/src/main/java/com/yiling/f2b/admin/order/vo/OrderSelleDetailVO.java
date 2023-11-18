package com.yiling.f2b.admin.order.vo;

import java.util.List;

import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售订单详情VO
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderSelleDetailVO extends OrderSellerAndPurchaseDetailCommonVO {
    @ApiModelProperty(value = "收货人地址信息")
    private OrderAddressVO orderAddress;

    /**
     * 回执单信息
     */
    @ApiModelProperty(value = "回执单信息")
    private List<FileInfoVO> receiveReceiptList;

}
