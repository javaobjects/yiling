package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSpecialSaveRequest extends BaseRequest {

    /**
     * 活动id
     */
    private Long id;

    /**
     * 促销活动信息
     */
    private PromotionSpecialActivitySaveRequest promotionSpecialActivitySave;


    /**
     * 促销活动企业
     */
    private List<PromotionSpecialEnterpriseSaveRequest> enterpriseSaves;


}
