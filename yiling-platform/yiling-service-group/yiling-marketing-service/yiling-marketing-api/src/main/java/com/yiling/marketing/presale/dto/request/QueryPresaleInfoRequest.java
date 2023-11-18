package com.yiling.marketing.presale.dto.request;

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
public class QueryPresaleInfoRequest extends BaseRequest {

    /**
     * 买家eid
     */
    private Long buyEid;

    /**
     * 1-B2B；2-销售助手
     */
    private Integer platformSelected;

    /**
     * goodsId集合
     */
    private List<Long> goodsId;

    /**
     * memberId
     */
    private Long memberId;

    /**
     * memberId==多会员
     */
    private List<Long> memberIds;

    /**
     * 预售活动id，订单模块专用
     */
    private Long presaleId;
}
