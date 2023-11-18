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
public class InstructionsHealthRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;
    /**
     * 商品Id
     */
    private Long   goodsId;
    /**
     * 原料
     */
    private String rawMaterial;

    /**
     * 辅料
     */
    private String ingredients;

    /**
     * 适宜人群
     */
    private String suitablePeople;

    /**
     * 不适宜人群
     */
    private String unsuitablePeople;

    /**
     * 保健功能
     */
    private String healthcareFunction;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 食用量及食用方法
     */
    private String usageDosage;

    /**
     * 储藏
     */
    private String store;

}
