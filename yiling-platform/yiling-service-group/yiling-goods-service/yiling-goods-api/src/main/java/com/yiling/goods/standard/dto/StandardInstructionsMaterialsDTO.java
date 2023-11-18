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
public class StandardInstructionsMaterialsDTO extends BaseDTO {

    /**
     * 性状
     */
    private String drugProperties;

    /**
     * 性味
     */
    private String propertyFlavor;

    /**
     * 功效
     */
    private String effect;

    /**
     * 用法与用量
     */
    private String usageDosage;

    /**
     * 保证期
     */
    private String expirationDate;

    /**
     * 储藏
     */
    private String store;

}
