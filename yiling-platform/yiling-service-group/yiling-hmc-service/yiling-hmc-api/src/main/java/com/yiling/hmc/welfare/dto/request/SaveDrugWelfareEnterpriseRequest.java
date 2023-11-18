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
public class SaveDrugWelfareEnterpriseRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * 福利计划id
     */
    private Long drugWelfareId;

    /**
     * 福利计划id
     */
    private String remake;
}
