package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退货单分页查询
 *
 * @author: yong.zhang
 * @date: 2022/3/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnPageListRequest extends QueryPageListRequest {

    /**
     * 查询排序方式：默认不传或者0则按照创建时间的倒叙查询；1.销售订单里面按照未审核，审核驳回，审核通过，创建时间升序 这种模式查询
     */
    private Integer sortType;

    /**
     * 退货单来源 1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private ReturnSourceEnum returnSourceEnum;

    /**
     * 订单类型枚举
     */
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
     * 供应商名称
     */
    private String sellerEname;

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
    private List<Long> sellerEidList;

    /**
     * 企业id
     */
    private List<Long> buyerEidList;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 部门id
     */
   private Long departmentId;
}
