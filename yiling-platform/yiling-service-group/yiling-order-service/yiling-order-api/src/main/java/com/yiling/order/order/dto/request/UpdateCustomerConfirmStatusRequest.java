package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.order.order.enums.CustomerConfirmEnum;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2022/1/20
 */
@Data
@Accessors(chain=true)
public class UpdateCustomerConfirmStatusRequest extends BaseRequest {
    /**
     * 确认订单
     */
    private List<UpdateConfirmStatusDetailRequest> confirmRequestList;

    @Data
    @Accessors(chain=true)
    public static class UpdateConfirmStatusDetailRequest extends BaseRequest {

        /**
         * 订单ID
         */
        private Long                orderId;
        /**
         * 原始确认状态
         */
        private CustomerConfirmEnum originalStatus;

        /**
         * 新的确认状态
         */
        private CustomerConfirmEnum newStatus;
        /**
         * 操作人
         */
        private Long                updateUserId;

        /**
         * 备注
         */
        private String              remark;
    }
}
