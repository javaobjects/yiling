package com.yiling.dataflow.gb.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateGbAppealFormFlowMatchNumberRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 流向匹配条数
     */
    private BigDecimal flowMatchNumber;

}
