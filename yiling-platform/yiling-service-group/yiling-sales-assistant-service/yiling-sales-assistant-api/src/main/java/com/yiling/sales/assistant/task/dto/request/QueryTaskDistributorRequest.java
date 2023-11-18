package com.yiling.sales.assistant.task.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根据配送商eid查询任务里配置的配送商
 *
 * @author: gxl
 * @date: 2022/10/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryTaskDistributorRequest extends BaseRequest {
    private static final long serialVersionUID = -5789390275010175208L;
    /**
     * 配送商id
     */
    private List<Long> eidList;

    private Long userId;
}