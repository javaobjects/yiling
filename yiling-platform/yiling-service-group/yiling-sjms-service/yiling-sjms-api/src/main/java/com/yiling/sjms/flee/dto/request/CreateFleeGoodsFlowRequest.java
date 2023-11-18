package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/16 0016
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateFleeGoodsFlowRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 调整流向的月份
     */
    private String toMonth;
}
