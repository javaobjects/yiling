package com.yiling.dataflow.gb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GbAppealSubstractCancleRequest extends BaseRequest {

    /**
     * 团购处理id
     */
    private Long appealFormId;

    /**
     * 源流向Id
     */
    private Long flowWashId;

}
