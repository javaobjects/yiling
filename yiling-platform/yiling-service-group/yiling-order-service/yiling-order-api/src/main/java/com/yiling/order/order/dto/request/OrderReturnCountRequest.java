package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询丰富和条件的退货单数量
 *
 * @author: yong.zhang
 * @date: 2021/11/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnCountRequest extends BaseRequest {

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer returnSource;

    /**
     * 采购商id
     */
    private Long buyerEid;

    /**
     * 供应商id
     */
    private Long sellerEid;

    /**
     * 退货单状态：1-待审核 2-审核通过 3-审核驳回
     */
    private Integer returnStatus;
}
