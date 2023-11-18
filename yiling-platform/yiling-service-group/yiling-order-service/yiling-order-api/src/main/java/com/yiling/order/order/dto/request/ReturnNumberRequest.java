package com.yiling.order.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.enums.ReturnSourceEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReturnNumberRequest extends BaseRequest {

    /**
     * 商务联系人id
     */
    private List<Long>       userIdList;

    /**
     * 采购商企业id
     */
    private List<Long>       buyerIdList;

    /**
     * 供应商企业id
     */
    private List<Long>       sellerIdList;

    /**
     * 退货单来源
     */
    private ReturnSourceEnum returnSourceEnum;

    private OrderTypeEnum    orderTypeEnum;

    /**
     * 部门Id
     */
    private Long departmentId;
}
