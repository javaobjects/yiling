package com.yiling.hmc.insurance.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsurancePageRequest extends QueryPageListRequest {

    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;

    /**
     * 保险名称
     */
    private String insuranceName;

    /**
     * 创建时间查询开始时间
     */
    private Date startTime;

    /**
     * 创建时间查询截止时间
     */
    private Date stopTime;
}
