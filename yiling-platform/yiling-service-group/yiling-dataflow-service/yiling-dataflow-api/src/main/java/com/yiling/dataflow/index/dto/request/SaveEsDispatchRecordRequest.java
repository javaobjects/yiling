package com.yiling.dataflow.index.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveEsDispatchRecordRequest extends BaseRequest {

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
