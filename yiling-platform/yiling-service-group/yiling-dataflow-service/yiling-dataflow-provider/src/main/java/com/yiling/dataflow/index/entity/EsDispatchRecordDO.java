package com.yiling.dataflow.index.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("es_dispatch_record")
public class EsDispatchRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务开始时间
     */
    private Date startTime;

    /**
     * 任务结束时间
     */
    private Date endTime;

    /**
     * 一共处理条数
     */
    private Long count;

    /**
     * 花费多少时间
     */
    private Long spendTime;

    /**
     * 备注
     */
    private String remark;


}
