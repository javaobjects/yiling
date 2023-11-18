package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateReportConsumeStatusRequest extends BaseRequest {

    private static final long serialVersionUID = 3939327261024888761L;

    /**
     * 月流向任务id
     */
    private Long id;

    /**
     * 报表生成是否已消费 0-否 1-是
     */
    private Integer reportConsumeStatus;
}
