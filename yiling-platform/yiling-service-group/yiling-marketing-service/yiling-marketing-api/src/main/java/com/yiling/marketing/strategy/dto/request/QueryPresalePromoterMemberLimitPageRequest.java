package com.yiling.marketing.strategy.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class QueryPresalePromoterMemberLimitPageRequest extends QueryPageListRequest {

    /**
     * 营销活动id
     */
    private Long marketingPresaleId;

    /**
     * 企业ID-精确搜索
     */
    private Long eid;

    private List<Long> eidList;

    /**
     * 企业名称-模糊搜索
     */
    private String ename;
}
