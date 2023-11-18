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
public class InstructionsDisinfectionRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 商品Id
     */
    private Long   goodsId;

    /**
     * 成分
     */
    private String drugDetails;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 使用方法
     */
    private String usageDosage;

    /**
     * 灭菌类别
     */
    private String sterilizationCategory;


}
