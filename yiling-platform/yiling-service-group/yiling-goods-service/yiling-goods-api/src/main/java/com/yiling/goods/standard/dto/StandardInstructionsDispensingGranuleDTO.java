package com.yiling.goods.standard.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 StandardInstructionsDispensingGranuleDTO
 * @描述 配方颗粒
 * @创建时间 2023/5/17
 * @修改人 shichen
 * @修改时间 2023/5/17
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsDispensingGranuleDTO extends BaseDTO {

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
