package com.yiling.marketing.couponactivityautogive.dto.request;

import java.util.List;

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
public class UpdateAutoGiveCountRequest extends BaseRequest {

    /**
     * id
     */
    private List<Long> ids;

}
