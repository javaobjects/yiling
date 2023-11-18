package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/5/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InstructionsFoodsRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 商品Id
     */
    private Long   goodsId;

    /**
     * 配料
     */
    private String ingredients;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 储藏
     */
    private String store;

    /**
     * 致敏源信息
     */
    private String allergens;


}
