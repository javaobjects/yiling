package com.yiling.marketing.promotion.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 促销活动主表新增请求参数
 *
 * @author: yong.zhang
 * @date: 2021/11/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SpecialActivityInfoRequest extends QueryPageListRequest {
    /**
     * 商户名称
     */
    private String ename;

    /**
     * 商户id集合
     */
    private List<Long>eids;

    /**
     * 当前用户的商户id
     */
    private Long currentEid;

    /**
     * 当前用户的商户id
     */
    private Long userId;

    /**
     * 活动类型 1-满赠，2-特价，3-秒杀, 4-组合包
     */
    private Integer type;

    /**
     * 营销活动id
     */
    private Long promotionActivityId;

    /**
     * 产品名称
     */
    private String goodsName;

    /**
     * 商户id-卖家id
     */
    private Long eid;
}
