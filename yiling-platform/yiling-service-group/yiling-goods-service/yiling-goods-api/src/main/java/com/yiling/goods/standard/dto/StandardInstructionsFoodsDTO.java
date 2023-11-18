package com.yiling.goods.standard.dto;

import com.yiling.framework.common.base.BaseDTO;

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
public class StandardInstructionsFoodsDTO extends BaseDTO {

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
