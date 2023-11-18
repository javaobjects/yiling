package com.yiling.dataflow.flow.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/1/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowSaleMatchRequest extends BaseRequest {

    private static final long serialVersionUID = 8141794692683937510L;

    /**
     * 商业公司id
     */
    private Long eid;

    /**
     * 单号
     */
    private String soNo;
}
