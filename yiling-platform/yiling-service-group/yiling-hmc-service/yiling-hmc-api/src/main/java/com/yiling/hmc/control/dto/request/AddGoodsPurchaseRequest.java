package com.yiling.hmc.control.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2022/4/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddGoodsPurchaseRequest extends BaseRequest {
    /**
     * 管控表id
     */
    private Long goodsControlId;

    /**
     * 供应商eid
     */
    private Long sellerEid;

    /**
     * 1-线上 2-线下
     */
    private Integer channelType;

}