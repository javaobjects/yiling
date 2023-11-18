package com.yiling.dataflow.order.dto.request;

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
public class QueryFlowGoodsBatchRequest extends BaseRequest {

    /**
     * 商业公司ID
     */
    private Long eid;

    /**
     * op库主键
     */
    private String gbIdNo;

    /**
     * 商品内码
     */
    private String inSn;

    /**
     * 商品规格
     */
//    private String gbSpecifications;

}
