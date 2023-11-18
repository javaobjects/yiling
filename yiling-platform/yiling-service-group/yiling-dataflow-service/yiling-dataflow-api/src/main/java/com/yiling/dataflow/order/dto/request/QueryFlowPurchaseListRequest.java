package com.yiling.dataflow.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseListRequest extends BaseRequest {

    // 采购年月
    private String time;

    // 采购商业渠道类型
    private Integer purchaseChannelId;

    // 供应商渠道类型
    private Integer supplierChannelId;

    // 采购商业Id列表
    private List<Long> purchaseEnterpriseIds;

    // 供应商Id列表
    private List<Long> supplierEnterpriseIds;

    private List<String> poMonthList;


}
