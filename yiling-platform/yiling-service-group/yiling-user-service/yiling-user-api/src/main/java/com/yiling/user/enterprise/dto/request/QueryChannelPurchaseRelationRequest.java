package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询渠道采购关系列表 Requset
 *
 * @author: yuecheng.chen
 * @date: 2021/6/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryChannelPurchaseRelationRequest extends BaseRequest {

    /**
     * 采购渠道商ID
     */
    private Long buyerChannelId;


    /**
     * 销售渠道商ID
     */
    private Long sellerChannelId;

}
