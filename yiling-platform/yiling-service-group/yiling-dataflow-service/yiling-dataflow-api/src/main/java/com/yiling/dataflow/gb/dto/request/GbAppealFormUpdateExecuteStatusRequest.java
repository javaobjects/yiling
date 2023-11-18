package com.yiling.dataflow.gb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealFormUpdateExecuteStatusRequest extends BaseRequest {

    /**
     * Id
     */
    private Long id;

    /**
     * 处理类型：1自动2人工
     */
    private Integer execType;

    /**
     * 处理状态:1-未开始、2-自动处理中、3-已处理、4-处理失败、5-手动处理中。数据字典：gb_appeal_form_data_exec_status
     */
    private Integer dataExecStatus;

}
