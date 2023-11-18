package com.yiling.dataflow.flowcollect.dto.request;

import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDayCollectStatisticsRequest extends QueryPageListRequest {
    /**
     * 企业编码
     */
    private Long crmEnterpriseId;
    /**
     * 经销商等级
     */
    private Integer supplierLevel;
    /**
     * 流向收集 方式
     */
    private Integer flowMode;
    /**
     * 流向收集类型
     */
    private Integer flowType;
    /**
     * 实施人员
     */
    private String installEmployee;

    private SjmsUserDatascopeBO userDatascopeBO;
    /**
     * 上传状态 1-近3天有一项未上传、2-近3天全部未上传、3-近5天有一项未上传、4-近5天全部未上传
     */
    private Integer uploadStatus;

}
