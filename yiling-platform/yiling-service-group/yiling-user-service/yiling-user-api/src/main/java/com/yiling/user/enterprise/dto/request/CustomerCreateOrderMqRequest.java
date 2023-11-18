package com.yiling.user.enterprise.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;

/**
 * @author shichen
 * @类名 CustomerCreateOrderMqRequest
 * @描述
 * @创建时间 2022/11/4
 * @修改人 shichen
 * @修改时间 2022/11/4
 **/
@Data
public class CustomerCreateOrderMqRequest extends BaseRequest {
    /**
     * 客户eid
     */
    private Long customerEid;

    /**
     * 卖家eid
     */
    private Long sellerEid;

    /**
     * 下单时间
     */
    private Date orderCreateTime;
}
