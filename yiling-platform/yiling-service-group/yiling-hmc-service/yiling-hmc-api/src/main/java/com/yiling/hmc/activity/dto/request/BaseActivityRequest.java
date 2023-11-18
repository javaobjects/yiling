package com.yiling.hmc.activity.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: fan.shen
 * @date: 2023/01/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BaseActivityRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

}
