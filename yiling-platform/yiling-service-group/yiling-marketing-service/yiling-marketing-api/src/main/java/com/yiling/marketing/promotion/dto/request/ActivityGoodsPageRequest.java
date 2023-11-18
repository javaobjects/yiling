package com.yiling.marketing.promotion.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/12/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ActivityGoodsPageRequest extends QueryPageListRequest {

    /**
     * 满赠活动id
     */
    private Long id;

    /**
     * 店铺id
     */
    private Long shopEid;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 活动类型 4组合包
     */
    private String type;
}
