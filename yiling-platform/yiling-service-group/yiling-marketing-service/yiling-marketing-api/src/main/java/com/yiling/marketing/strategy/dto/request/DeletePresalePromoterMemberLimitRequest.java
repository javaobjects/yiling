package com.yiling.marketing.strategy.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeletePresalePromoterMemberLimitRequest extends BaseRequest {

    /**
     * 营销活动id
     */
    private Long marketingPresaleId;

    /**
     * 企业id-单独删除时使用
     */
    private Long eid;

    /**
     * 企业id集合-批量删除时使用
     */
    private List<Long> eidList;
}
