package com.yiling.dataflow.statistics.dto.request;

import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockReferenceTimeRequest extends BaseRequest {
    //EID
    private Long eid;
    private Long crmEnterpriseId;
    //商品规格ID
    private Long specificationId;
    //补货天数
    private Long replenishDays;
    //参考天数
    private Long referenceTime;
    //初始化销售天数
    List<PeriodDaySaleQuantityBO> stockForastDayList;

    /**
     * 当前库存数量
     */
    private Long curStockQuantity;
    /**
     * 建议安全库存
     */
    private Long safeStockQuantity;
    /**
     * 日均销量
     */
    private Long aleSaleDays;
}
