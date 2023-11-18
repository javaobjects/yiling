package com.yiling.sales.assistant.task.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务追踪成员明细
 * </p>
 *
 * @author gxl
 * @since 2021-09-17
 */
@Data
@Accessors(chain = true)
public class TaskTraceUserDTO implements Serializable {


    private static final long serialVersionUID = 3905732587296448766L;
    private Long userId;

    private String userName;

    private String mobile;


    private Long taskId;

    private Long userTaskId;


    private Integer finishType;

    private String finishValue;

    private Integer finishGoods;

    private Integer taskGoodsTotal;

    private String goal;

    private String percent;

    private Integer taskStatus;

    private Boolean eachCompute;

    /**
     * 创建时间
     */
    private Date createdTime;

    private Integer finishStep;

    private String commision;

    private List<String> percentList;
}
