package com.yiling.goods.standard.dto.request;

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
public class StandardInstructionsDecoctionRequest extends BaseRequest {

    /**
     * 中药饮片说明书id
     */
    private Long id;

    /**
     * 净含量
     */
    private String netContent;

    /**
     * 原产地
     */
    private String sourceArea;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 包装清单
     */
    private String packingList;

    /**
     * 用法与用量
     */
    private String usageDosage;

    /**
     * 储藏
     */
    private String store;
}
