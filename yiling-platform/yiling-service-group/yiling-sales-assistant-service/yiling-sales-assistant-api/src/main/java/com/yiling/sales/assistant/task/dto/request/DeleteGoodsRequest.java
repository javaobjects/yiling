package com.yiling.sales.assistant.task.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2021/09/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteGoodsRequest extends BaseRequest {

    private static final long serialVersionUID = 7096503254514001598L;

    /**
     * 任务与商品id
     */
    private Long taskGoodsId;

}
