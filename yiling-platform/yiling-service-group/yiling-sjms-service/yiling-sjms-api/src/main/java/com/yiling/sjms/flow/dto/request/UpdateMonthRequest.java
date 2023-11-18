package com.yiling.sjms.flow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMonthRequest extends BaseRequest {
    /**
     * 列表id
     */
    private Long id;

    /**
     * 补传的月流向
     */
    private String appealMonth;
}
