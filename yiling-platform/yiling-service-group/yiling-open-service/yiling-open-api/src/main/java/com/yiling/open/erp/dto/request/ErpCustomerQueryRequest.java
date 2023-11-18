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
public class ErpCustomerQueryRequest extends QueryPageListRequest {

    private Integer eid;
    /**
     * 企业名
     */
    private String name;

    /**
     * 企业类型
     */
    private String customerType;

    /**
     * 供应商id
     */
    private Long suId;

    /**
     * 唯一代码
     */
    private String licenseNo;

    /**
     * 同步信息
     */
    private String syncMsg;

    /**
     * 同步状态
     */
    private Integer syncStatus;

    /**
     * 查询条数
     */
    private Integer queryLimit;
}
