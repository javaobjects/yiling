package com.yiling.dataflow.wash.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseWashPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = -3436488791106291210L;

    /**
     * 流向清洗任务id
     */
    private Long fmwtId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    private Long crmEnterpriseId;

    private Long crmOrganizationId;

    private List<Long> crmEnterpriseIdList;

    private List<String> provinceCodes;

    private Date startTime;

    private Date endTime;

    private String enterpriseName;

    private String goodsName;

    private String goodsSpec;

    private Long crmGoodsCode;

    private List<Long> crmGoodsCodeList;

    private Integer mappingStatus;

    private Integer goodsMappingStatus;

    private Integer washStatus;

    private List<Integer> washStatusList;
}
