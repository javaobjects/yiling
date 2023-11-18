package com.yiling.mall.common.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存商品 request
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsRequest extends BaseRequest {

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 商品ID
     */
    private Long goodsId;
}
