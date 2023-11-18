package com.yiling.hmc.welfare.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteDrugWelfareEnterpriseRequest extends BaseRequest {
    /**
     * 福利计划与商家关系id
     */
    private Long id;
}
