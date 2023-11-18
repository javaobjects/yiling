package com.yiling.dataflow.agency.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2023/2/15 0015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveCrmPharmacyRequest extends BaseRequest {

    /**
     * 机构基本信息id
     */
    private Long crmEnterpriseId;
}
