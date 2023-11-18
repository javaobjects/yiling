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
public class AddPresaleBuyerLimitRequest extends BaseRequest {

    /**
     * 营销活动id
     */
    private Long marketingPresaleId;

    /**
     * 企业id-单独添加时使用
     */
    private Long eid;

    /**
     * 企业id集合-添加当前页时使用
     */
    private List<Long> eidList;

    /**
     * 企业ID-精确搜索-添加搜索结果时使用
     */
    private Long eidPage;

    /**
     * 企业名称-模糊搜索-添加搜索结果时使用
     */
    private String enamePage;
}
