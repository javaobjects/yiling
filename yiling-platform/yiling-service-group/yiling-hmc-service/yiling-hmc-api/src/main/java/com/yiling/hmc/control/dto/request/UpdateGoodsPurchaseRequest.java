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
public class UpdateGoodsPurchaseRequest extends BaseRequest {

    private static final long serialVersionUID = 5630481783598844250L;
    private Long id;
    /**
     * 1-线上 2-线下
     */
    private Integer channelType;

    /**
     * 0-关闭 1-开启
     */
    private Integer controlStatus;

}