package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 获取销售助手确认订单
 * @author:wei.wang
 * @date:2021/6/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAssistantConfirmOrderPageRequest extends QueryPageListRequest {
    /**
     * 开始下单时间
     */
    private Date startCreateTime;

    /**
     * 结束下单时间
     */
    private Date endCreateTime;


    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;


    /**
     * 商务联系人
     */
    private List<Long> contacterIdList;

    /**
     * 下单人
     */
    private Long createUser;

}
