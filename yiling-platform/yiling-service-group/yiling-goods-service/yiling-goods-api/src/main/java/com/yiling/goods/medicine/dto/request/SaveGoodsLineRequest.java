package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGoodsLineRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * b2b是否开通：0不开通 1开通
     */
    private Integer mallFlag;

    /**
     * pop是否开通：0不开通 1开通
     */
    private Integer popFlag;
}
