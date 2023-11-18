package com.yiling.sales.assistant.task.dto.app;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * app任务列表页实体
 * </p>
 *
 * @author gxl
 * @since 2021-09-27
 */
@Data
@Accessors(chain = true)
public class TaskDTO implements Serializable {


    private static final long serialVersionUID = -1646108334278183908L;
    private Long id;

    private String taskName;


    private Integer goodsCount;

    private String profit;
    private Integer neverExpires;

    private Date startTime;

    private Date endTime;

    private String takeCount;

    private String takeTimes;

    private String taskRule;


    private Integer fullCover;

    private Boolean taked;

    private Integer finishType;

}
