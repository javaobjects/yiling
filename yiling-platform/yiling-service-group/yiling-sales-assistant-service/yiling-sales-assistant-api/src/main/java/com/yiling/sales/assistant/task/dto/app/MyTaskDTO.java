package com.yiling.sales.assistant.task.dto.app;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * app我的任务列表页实体
 * </p>
 *
 * @author gxl
 * @since 2021-09-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class MyTaskDTO extends BaseDTO {



    private Long taskId;

    private String taskName;

    private Integer goodsCount;
    private Integer saleType;

    private String profit;

    private Integer neverExpires;

    private Date startTime;

    private Date endTime;

    private String takeCount;

    private String takeTimes;

    private Integer finishType;

    private Integer taskStatus;
}
