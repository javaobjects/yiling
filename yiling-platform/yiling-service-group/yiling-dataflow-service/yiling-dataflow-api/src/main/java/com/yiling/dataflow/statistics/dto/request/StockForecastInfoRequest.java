package com.yiling.dataflow.statistics.dto.request;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.statistics.bo.PeriodDaySaleQuantityBO;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockForecastInfoRequest extends BaseRequest {
   @Deprecated
    private Long eid;
    private Long crmEnterpriseId;
    private String enameGroup;
    private String enameLevel;
    private Long specificationId;
    private Long replenishDays;
    private Long referenceTime1;
    private Long referenceTime2;
    private Long referenceTime;
       //初始化销售天数
    private List<PeriodDaySaleQuantityBO> stockForastDayList;
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

    private Integer businessSystem;
    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;
    /**
     * 数据权限控制
     */
    private List<Long> longs;

    private SjmsUserDatascopeBO sjmsUserDatascopeBO;
}
