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
public class StandardInstructionsDisinfectionDTO extends BaseDTO {

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
