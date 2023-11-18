package com.yiling.open.backup.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2023/1/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowSaleDeleteBackRequest extends BaseRequest {

    /**
     * 供应商id
     */
    protected Long suId;

    /**
     * 供应商部门
     */
    protected String suDeptNo="";

    /**
     * op库主键
     */
    private String soId;

    /**
     * 购进时间
     */
    private Date soTime;

}
