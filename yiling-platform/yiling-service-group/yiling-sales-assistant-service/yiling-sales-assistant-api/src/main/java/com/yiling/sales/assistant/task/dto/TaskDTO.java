package com.yiling.sales.assistant.task.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务列表页实体
 * </p>
 *
 * @author gxl
 * @since 2020-04-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskDTO extends BaseDTO {


    private static final long serialVersionUID = -8981853549540109602L;
    private String taskName;

    private String taskDesc;

    private Integer saleType;

    private Integer categories;

    private String ename;

    private Long eid;

    private String taskArea;

    private Integer takeLimit;

    private Integer timesLimit;


    private Integer neverExpires;

    private Date startTime;

    private Date endTime;

    private Integer taskStatus;

    private String createdBy;



    private Date createdTime;

    private String updatedBy;


    private Date updatedTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改人
     */
    private Long updateUser;

    private Integer category;

    private Integer finishType;
}
