package com.yiling.sales.assistant.task.dto.request.app;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *
 * @author: ray
 * @date: 2021/9/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskGoodsRequest extends BaseRequest {
    private static final long serialVersionUID = 7526908920278498067L;
    private Long taskId;
}