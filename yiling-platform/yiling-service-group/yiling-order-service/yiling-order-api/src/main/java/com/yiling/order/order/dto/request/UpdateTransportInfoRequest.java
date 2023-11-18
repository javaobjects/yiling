package com.yiling.order.order.dto.request;



import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改物流信息
 * @author:wei.wang
 * @date:2023/5/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTransportInfoRequest extends BaseRequest {

    private Integer deliveryType;


    private String deliveryCompany;


    private String deliveryNo;


    private Long orderId;
}
