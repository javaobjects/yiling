package com.yiling.dataflow.statistics.dto.request;

import java.util.Date;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryBalanceStatisticsPageRequest extends QueryPageListRequest {

    private Long crmEnterpriseId;

    private SjmsUserDatascopeBO userDatascopeBO;

    private Date startDateTime;

    private Date endDateTime;

    private Integer enameLevel;

    private Integer flowMode;
}
