package com.yiling.dataflow.wash.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpClientWashPlanDTO extends BaseDTO {

    private Long crmEnterpriseId;

    private Long eid;

    /**
     * 对接方式
     */
    private Integer flowMode;

    private String name;

    private Long fmwcId;

    /**
     * 计划清洗时间
     */
    private Date planTime;

    /**
     * 状态：0-未发送1-已发送2-已接收
     */
    private Integer status;
}
