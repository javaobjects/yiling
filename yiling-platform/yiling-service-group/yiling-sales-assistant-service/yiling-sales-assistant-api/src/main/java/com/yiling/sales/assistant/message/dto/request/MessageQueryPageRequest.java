package com.yiling.sales.assistant.message.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MessageQueryPageRequest extends QueryPageListRequest {
    /**
     * 业务类型，0 所有，1-业务进度，2-发布任务，3-促销政策，4-学术下方
     */
    private Integer type;

    /**
     * 用户id
     */
    private Long    userId;
}
