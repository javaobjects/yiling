package com.yiling.user.enterprise.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询采购关系分页列表 Requset
 *
 * @author: yuecheng.chen
 * @date: 2021/6/7 0007
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPurchaseRelationPageListRequest extends QueryPageListRequest {
    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 采购渠道商ID
     */
    private Long buyerChannelId;

    /**
     * 渠道商ID
     */
    private Long sellerEid;

    /**
     * 销售渠道商ID
     */
    private Long sellerChannelId;

    /**
     * 渠道商名称
     */
    private String sellerName;

    /**
     * 认证状态：1-未认证 2-认证通过 3-认证不通过
     */
    private Integer authStatus;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 类型：0-以岭直采，1-一级商，2-二级商
     */
    private Integer purchaseRelationType;
}
