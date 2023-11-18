package com.yiling.dataflow.order.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/6/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryParamYlGoodsListRequest extends BaseRequest {

    /**
     * 商业id
     */
    private Long eid;

    /**
     * 以岭品名称
     */
    private String ylGoodsName;

}
