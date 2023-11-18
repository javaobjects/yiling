package com.yiling.hmc.settlement.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceSettlementPageRequest extends QueryPageListRequest {

    /**
     * 保险提供服务商名称
     */
    private String insuranceCompanyName;

    /**
     * 结账日期段-起始时间
     */
    private Date startTime;

    /**
     * 结账日期段-截止时间
     */
    private Date stopTime;
}
