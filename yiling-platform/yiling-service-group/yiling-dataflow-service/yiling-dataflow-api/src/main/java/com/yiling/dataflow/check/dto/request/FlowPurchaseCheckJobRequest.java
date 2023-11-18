package com.yiling.dataflow.check.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowPurchaseCheckJobRequest extends BaseRequest {

    /**
     *
     */
    private Date taskTime;
}
