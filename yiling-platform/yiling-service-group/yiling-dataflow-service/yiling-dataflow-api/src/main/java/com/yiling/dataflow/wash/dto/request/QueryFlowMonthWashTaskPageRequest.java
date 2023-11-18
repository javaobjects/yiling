package com.yiling.dataflow.wash.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowMonthWashTaskPageRequest extends QueryPageListRequest {

    private static final long serialVersionUID = 2699609447886278314L;

    private Integer flowType;

    private Integer year;

    private Integer month;

    private Long crmEid;

    private List<Long> crmEids;

    private List<String> provinceCodes;

    private Integer washStatus;

    private Integer confirmStatus;

    private Integer flowClassify;
}
