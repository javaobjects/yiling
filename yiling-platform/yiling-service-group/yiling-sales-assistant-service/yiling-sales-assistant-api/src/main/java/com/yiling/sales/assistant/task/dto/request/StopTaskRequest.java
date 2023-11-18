package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author:gxl
 * @description:
 * @date: Created in 19:18 2020/5/8
 * @modified By:
 */
@Data
public class StopTaskRequest extends BaseRequest {

    private static final long serialVersionUID = 1304766123408523934L;
    private Long id;

}
