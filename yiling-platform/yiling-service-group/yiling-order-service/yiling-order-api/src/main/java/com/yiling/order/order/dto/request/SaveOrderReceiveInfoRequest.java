package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 收货明货物明细
 *
 * @author:wei.wang
 * @date:2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrderReceiveInfoRequest extends BaseRequest {


    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 收货批次内容
     */
    private List<ReceiveInfoRequest> receiveInfoList;

}
