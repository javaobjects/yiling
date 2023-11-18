package com.yiling.dataflow.statistics.dto;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 库存预警图标部分
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockWarnIconDTO extends BaseDTO {
    //private Long eid;
    private Long crmEnterpriseId;
    private String ename;
    private String enameLevel;
    private String goodsName;
    private String specificationId;//商品规则ID
    private Long saleAvg;// 日均销量
    private BigDecimal supportDay;//平均天数
    private Long stockQuantity;//库存数量


}
