package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.goods.medicine.enums.GoodsLineStatusEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchUpdateGoodsStatusRequest extends BaseRequest {
    /**
     * 产品线
     */
    private Integer goodsLine;
    /**
     * 商品ID集合
     */
    private List<Long> goodsIds;
    /**
     * 商品状态
     */
    private Integer goodsStatus;
    /**
     * 下架原因
     */
    private Integer outReason;

    /**
     * 0 不启用 1启用
     */
    private GoodsLineStatusEnum goodsLineStatusEnum;
}
