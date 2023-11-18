package com.yiling.dataflow.statistics.dto.request;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StockWarnPageRequest   extends QueryPageListRequest {
   // private Long eid;
    private Long crmEnterpriseId;
    private String enameGroup;
    private String enameLevel;
    private Long specificationId;
    private String sort;
    private Integer iconTab;
   // private List<Long> eidsList;
    private List<Long> crmIdList;
    /**
     * 商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    private Integer supplierLevel;
    /**
     * 商业体系
     */
    private Integer businessSystem;
    private List<Long> longs;
    private SjmsUserDatascopeBO sjmsUserDatascopeBO;
}
