package com.yiling.sales.assistant.task.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务区域（前端反显用）
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Data
@Accessors(chain = true)
@TableName("sa_task_area_json")
public class TaskAreaJsonDO  {


    @TableId
    private Long taskId;

    private String areaJson;


}
