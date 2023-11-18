package com.yiling.dataflow.statistics.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2022/12/29
 */
@Data
public class DaySaleStatisticsBO {

    private Long eid;

    private Long crmEnterpriseId;

    private Long specId;

    private Date dateTime;

    private BigDecimal quantity;
}
