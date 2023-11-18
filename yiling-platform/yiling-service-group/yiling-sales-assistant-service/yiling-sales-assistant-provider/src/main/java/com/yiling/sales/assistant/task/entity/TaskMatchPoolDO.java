package com.yiling.sales.assistant.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务和随货同行单匹配池
 * </p>
 *
 * @author gxl
 * @date 2023-01-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_match_pool")
public class TaskMatchPoolDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 随货同行单id
     */
    private Long accompanyingBillId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
