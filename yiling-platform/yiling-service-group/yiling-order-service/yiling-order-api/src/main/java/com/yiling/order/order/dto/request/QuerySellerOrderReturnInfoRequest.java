package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.order.order.enums.ReturnSourceEnum;

import com.yiling.order.order.enums.OrderTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退货单列表查询请求参数
 *
 * @author: wei.wang
 * @date: 2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySellerOrderReturnInfoRequest extends QueryPageListRequest {

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private ReturnSourceEnum returnSourceEnum;

    private OrderTypeEnum orderTypeEnum;

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 退货单号
     */
    private String orderReturnNo;

    /**
     * 采购商名称
     */
    private String buyerEname;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
     */
    private Integer returnType;

    /**
     * 订单状态 1-待审核 2-审核通过 3-审核驳回
     */
    private Integer returnStatus;

    /**
     * 商务联系人集合
     */
    private List<Long> userIdList;

    /**
     * 企业id
     */
    private List<Long> eidList;
}
