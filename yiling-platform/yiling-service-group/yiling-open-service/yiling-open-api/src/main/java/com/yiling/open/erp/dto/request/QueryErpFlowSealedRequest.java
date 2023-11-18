package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpFlowSealedRequest extends BaseRequest {

    /**
     * 商业id
     */
    private Long eid;

    /**
     * 流向类型
     */
    private Integer type;

    /**
     * 封存月份
     */
    private String month;

}
