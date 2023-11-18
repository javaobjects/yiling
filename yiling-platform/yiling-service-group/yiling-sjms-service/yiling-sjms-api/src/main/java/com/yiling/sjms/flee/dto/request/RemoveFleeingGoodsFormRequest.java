package com.yiling.sjms.flee.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/3/13 0013
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveFleeingGoodsFormRequest extends BaseRequest {

    /**
     * 窜货申诉表 id
     */
    private Long id;
}
