package com.yiling.dataflow.flowcollect.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowCollectUnUploadRequest extends BaseRequest {

    /**
     * 商业公司编码
     */
    private Long crmEnterpriseId;

    /**
     * 统计的时间
     */
    private Date statisticsTime;

    private Date startDate;

    private Date endDate;

    private List<Long> crmEnterpriseIdList;

}
