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
public class StandardInstructionsGoodsBaseRequest extends BaseRequest {

    /**
     * 普通药品说明书信息
     */
    private Long id;

    /**
     * 药品成分
     */
    private String drugDetails;

    /**
     * 药品性状
     */
    private String drugProperties;

    /**
     * 适应症
     */
    private String indications;

    /**
     * 用法与用量
     */
    private String usageDosage;

    /**
     * 不良反应
     */
    private String adverseEvents;

    /**
     * 禁忌症
     */
    private String contraindication;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 药物相互作用
     */
    private String interreaction;

    /**
     * 存储条件
     */
    private String storageConditions;

    /**
     * 保质期
     */
    private String shelfLife;

    /**
     * 执行标准
     */
    private String executiveStandard;
}
