package com.yiling.hmc.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsSaveListRequest extends BaseRequest {

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * C端保险药品商家提成信息
     */
    private List<GoodsSaveRequest> goodsRequest;
}
