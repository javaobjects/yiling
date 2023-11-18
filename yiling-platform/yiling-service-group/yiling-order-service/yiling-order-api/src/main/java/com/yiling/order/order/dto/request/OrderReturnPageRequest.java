package com.yiling.order.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/10/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnPageRequest extends QueryPageListRequest {

    /**
     * 退货单号和供应商名称  退货号-精确查询，供应商名称-模糊查询
     */
    private String condition;

    /**
     * 当前操作人
     */
    private Long currentUserId;

    /**
     * 当前切换到的企业ID
     */
    private Long currentEid;

    /**
     * 退货单状态：0-全部 1-待审核 2-已通过 3-已驳回
     */
    private Integer returnStatus;

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer returnSource;

    /**
     * 退货单类型：1-POP退货单,2-B2B退货单
     */
    private Integer           orderReturnType;
}
