package com.yiling.marketing.promotion.dto.request;

import java.io.Serializable;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2021/11/4
 */
@Data
@Accessors(chain = true)
public class PromotionSpecialEnterpriseSaveRequest extends BaseRequest implements Serializable {

    /**
     * 专场活动id
     */
    private Long specialActivityId;

    /**
     * 企业ID
     */
    private Long eid;
    /**
     * 营销活动id
     */
    private Long promotionActivityId;

    /**
     * 专场活动图片
     */
    private String pic;
}
