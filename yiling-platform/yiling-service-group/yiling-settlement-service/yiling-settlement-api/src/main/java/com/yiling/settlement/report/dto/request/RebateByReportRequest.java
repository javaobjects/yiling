package com.yiling.settlement.report.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-08-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RebateByReportRequest extends BaseRequest {

    /**
     * 报表id
     */
    @NotNull
    private Long reportId;

    /**
     * 报表明细id列表
     */
    @NotNull
    private List<Long> detailIdList;
}
