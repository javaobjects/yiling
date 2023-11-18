package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpClientParentQueryRequest extends QueryPageListRequest {

    /**
     * 企业ID
     */
    private Integer rkSuId;


    /**
     * 企业名称
     */
    private String clientName;

}
