package com.yiling.dataflow.statistics.bo;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/2/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowDailyStatisticsBO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    private Long crmEnterpriseId;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 统计时间
     */
    private Date dateTime;

    private Long poRowNumber;

    private Long soRowNumber;

    private Long gbRowNumber;

    private Integer flowMode;

    private Integer supplierLevel;

    private String enameLevel;

    private Date collectTime;

}
