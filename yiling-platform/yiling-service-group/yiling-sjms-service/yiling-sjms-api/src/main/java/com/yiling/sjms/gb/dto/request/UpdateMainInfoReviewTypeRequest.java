package com.yiling.sjms.gb.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMainInfoReviewTypeRequest extends BaseRequest {

    /**
     * 表单ID
     */
    private Long gbId;

    /**
     * 核实团购性质：1-普通团购 2-政府采购
     */
    private Integer  gbReviewType;

    /**
     * 是否地级市下机构：1-是 2-否
     */
    private Integer gbCityBelow;
}
