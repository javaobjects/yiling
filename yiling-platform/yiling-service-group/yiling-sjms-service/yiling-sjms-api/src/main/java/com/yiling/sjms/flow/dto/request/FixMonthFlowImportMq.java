package com.yiling.sjms.flow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 补传月流向生成数据mq
 * @author: gxl
 * @date: 2023/6/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FixMonthFlowImportMq extends BaseRequest {
    private String fileName;
    private String fileKey;
    private Long monthFlowFormId;
}