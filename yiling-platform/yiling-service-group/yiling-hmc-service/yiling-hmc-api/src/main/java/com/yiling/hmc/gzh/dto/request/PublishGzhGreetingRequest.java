package com.yiling.hmc.gzh.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: fan.shen
 * @date: 2022/09/01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PublishGzhGreetingRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;
}
