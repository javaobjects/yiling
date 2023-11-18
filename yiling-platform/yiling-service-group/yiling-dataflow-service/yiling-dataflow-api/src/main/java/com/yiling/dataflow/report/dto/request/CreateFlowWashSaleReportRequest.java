package com.yiling.dataflow.report.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 根于源流向创建流向合并报表
 *
 * @author zhigang.guo
 * @date: 2023/6/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateFlowWashSaleReportRequest extends BaseRequest {

    /**
     * 源流向Key
     */
    @NotNull
    private String oldFlowKey;

    /**
     * 新流向Key
     */
    @NotNull
    private String flowKey;

    /**
     * 数量
     */
    @NotNull
    private BigDecimal qty;

    /**
     * 清洗时间
     */
    @NotNull
    private Date washTime;

    /**
     * 流向类型 {@link com.yiling.dataflow.wash.enums.FlowClassifyEnum}
     */
    @NotNull
    private Integer flowClassify;

    /**
     * 新生成的流向Id
     */
    @NotNull
    private Long flowWashId;

    /**
     * 申诉类型
     */
    @NotNull
    private Integer complainType;

    /**
     * 计入年份
     */
    @NotNull
    private Integer year;

    /**
     * 计入月份
     */
    @NotNull
    private Integer month;

}
