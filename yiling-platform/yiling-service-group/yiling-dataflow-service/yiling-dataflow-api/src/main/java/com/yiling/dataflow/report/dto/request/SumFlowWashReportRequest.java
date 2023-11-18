package com.yiling.dataflow.report.dto.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.yiling.dataflow.wash.enums.WashMappingStatusEnum;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SumFlowWashReportRequest extends BaseRequest {

    /**
     * 计入年份
     */
    @NotBlank
    private String year;

    /**
     * 计入月份
     */
    @NotBlank
    private String month;

    /**
     * 匹配状态 {@link WashMappingStatusEnum}
     */
    private Integer mappingStatus;

    /**
     * 流向是否锁定 1:是 2否
     */
    private Integer isLockFlag;

    /**
     * 流向类型 {@link com.yiling.dataflow.wash.enums.FlowClassifyEnum}
     */
    private List<Integer> flowClassifyList;

    /**
     * 客户crmId
     */
    private Long customerCrmId;

    /**
     * 商业crmId
     */
    private Long crmId;
}
