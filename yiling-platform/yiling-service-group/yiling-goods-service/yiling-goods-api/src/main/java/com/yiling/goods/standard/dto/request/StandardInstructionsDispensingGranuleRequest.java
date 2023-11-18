package com.yiling.goods.standard.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 StandardInstructionsDispensingGranuleRequest
 * @描述
 * @创建时间 2023/5/17
 * @修改人 shichen
 * @修改时间 2023/5/17
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StandardInstructionsDispensingGranuleRequest extends BaseRequest {

    /**
     * 说明书id
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
