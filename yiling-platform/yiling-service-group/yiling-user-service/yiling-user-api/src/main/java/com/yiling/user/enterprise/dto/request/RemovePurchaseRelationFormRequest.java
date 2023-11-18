package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 移除渠道商采购关系 requset
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8 0008
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemovePurchaseRelationFormRequest extends BaseRequest {
    /**
     * 采购商ID
     */
    private Long buyerId;

    /**
     * 供应商ID集合
     */
    private List<Long> sellerIds;

}
