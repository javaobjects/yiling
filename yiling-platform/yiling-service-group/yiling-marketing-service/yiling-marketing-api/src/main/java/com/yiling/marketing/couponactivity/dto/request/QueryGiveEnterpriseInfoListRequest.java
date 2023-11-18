package com.yiling.marketing.couponactivity.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/12/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGiveEnterpriseInfoListRequest extends BaseRequest {

    /**
     * 优惠券活动id
     */
    private Long couponActivityId;

    /**
     * 企业id
     */
    private List<Long> eidList;

}
