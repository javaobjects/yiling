package com.yiling.hmc.welfare.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

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
public class DrugWelfareEnterprisePageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 福利计划id
     */
    private Long drugWelfareId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;
}
