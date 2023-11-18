package com.yiling.dataflow.order.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/2/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteFlowGoodsBatchDetailRequest extends BaseRequest {

    /**
     * 商业公司ID
     */
    private Long eid;

    /**
     * 时间
     */
    private Date dateTime;

}
