package com.yiling.hmc.insurance.dto.request;

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
public class InsuranceCompanyPageRequest extends QueryPageListRequest {

    /**
     * 保险服务商名称
     */
    private String companyName;
}
