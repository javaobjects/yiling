package com.yiling.dataflow.wash.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.dataflow.wash.bo.ErpClientSimpleBO;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UpdateStageRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    private Long id;

}
