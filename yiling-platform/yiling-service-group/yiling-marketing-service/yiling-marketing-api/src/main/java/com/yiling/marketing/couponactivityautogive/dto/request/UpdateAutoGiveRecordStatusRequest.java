package com.yiling.marketing.couponactivityautogive.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateAutoGiveRecordStatusRequest extends BaseRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 发放状态（0-待发放；1-已发放；2-发放失败）
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String faileReason;

}
