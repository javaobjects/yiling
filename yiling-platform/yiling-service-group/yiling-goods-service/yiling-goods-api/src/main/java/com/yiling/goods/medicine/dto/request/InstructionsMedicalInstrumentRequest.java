package com.yiling.goods.medicine.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 InstructionsMedicalInstrumentRequest
 * @描述
 * @创建时间 2022/7/19
 * @修改人 shichen
 * @修改时间 2022/7/19
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InstructionsMedicalInstrumentRequest extends BaseRequest {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 组成结构
     */
    private String structure;

    /**
     * 注意事项
     */
    private String noteEvents;

    /**
     * 包装
     */
    private String packingInstructions;

    /**
     * 保质期
     */
    private String expirationDate;

    /**
     * 使用范围
     */
    private String useScope;

    /**
     * 使用方法
     */
    private String usageDosage;

    /**
     * 存储条件
     */
    private String storageConditions;

    /**
     * 备注
     */
    private String remark;
}
