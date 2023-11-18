package com.yiling.dataflow.wash.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class QueryUnlockThirdRecordPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 8853656109416359941L;

    /**
     * 客户机构编码
     */
    private Long orgCrmId;

    /**
     * 操作开始时间
     */
    private Date opStartTime;

    /**
     * 操作结束时间
     */
    private Date opEndTime;
}
