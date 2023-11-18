package com.yiling.hmc.welfare.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: benben.jia
 * @data: 2022/10/09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DrugWelfareGroupListRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 福利计划id
     */
    private Long drugWelfareId;

    /**
     * 商家id
     */
    private Long eid;


}
