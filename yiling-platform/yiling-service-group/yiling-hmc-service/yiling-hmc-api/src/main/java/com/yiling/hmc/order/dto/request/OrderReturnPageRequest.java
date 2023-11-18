package com.yiling.hmc.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnPageRequest extends QueryPageListRequest {

    /**
     * 退货单编号
     */
    private String returnNo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 第三方退单编号
     */
    private String thirdReturnNo;

    /**
     * 药品服务终端id
     */
    private Long eid;

    /**
     * 药品服务终端名称
     */
    private String ename;
}
