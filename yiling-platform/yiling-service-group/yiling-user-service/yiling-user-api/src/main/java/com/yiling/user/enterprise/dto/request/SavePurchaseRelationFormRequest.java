package com.yiling.user.enterprise.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存渠道商采购关系 requset
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8 0008
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SavePurchaseRelationFormRequest extends BaseRequest {
    /**
     * 采购商ID
     */
    private Long buyerId;

    /**
     * 供应商ID集合
     */
    private List<Long> sellerIds;

    /**
     * 来源：1-协议生成 2-手动创建
     */
    private Integer source;
}
