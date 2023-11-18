package com.yiling.dataflow.sale.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateConfigStatusRequest extends BaseRequest {

    /**
     * 指标ID
     */
    private Long saleTargetId;

    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 状态 1-未配置 2-已配置 3-配置中
     */
    private CrmSaleDepartmentTargetConfigStatusEnum configStatus;

    /**
     * 总分解数
     */
    private Integer goal;

    /**
     * 分解模板osskey
     */
    private String templateUrl;
}
