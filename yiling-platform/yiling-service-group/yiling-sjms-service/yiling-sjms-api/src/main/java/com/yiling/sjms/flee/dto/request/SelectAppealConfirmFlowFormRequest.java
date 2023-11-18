package com.yiling.sjms.flee.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * 选择流向数据主表数据
 * @author: xinxuan.jia
 * @date: 2023/6/25
 */
@Data
public class SelectAppealConfirmFlowFormRequest extends BaseRequest {

    /**
     * 选择申诉流向数据id
     */
    private Long selectFlowId;

    /**
     * 选择申诉源流向数据key
     */
    private String flowKey;

}
