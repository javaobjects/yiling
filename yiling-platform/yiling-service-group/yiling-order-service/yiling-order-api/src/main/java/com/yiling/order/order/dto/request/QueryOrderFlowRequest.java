package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/9/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderFlowRequest extends QueryPageListRequest {

    private String goodsName;

    private String buyerEname;

    private String sellerEname;

    private String licenseNo;

    private String sellSpecifications;

    private Long buyerChannelId;

    private Date startCreateTime;

    private Date endCreateTime;

    private Date startDeliveryTime;

    private Date endDeliveryTime;

    private List<Long> eidList;

}
