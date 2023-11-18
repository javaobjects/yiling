package com.yiling.admin.data.center.report.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2022-05-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryReportFlowDetailPageForm extends QueryPageListForm {

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String enterpriseName;

    /**
     * 报表id
     */
    @NotNull
    @ApiModelProperty(value = "报表id")
    private  Long reportId;

    /**
     * 开始销售时间
     */
    @ApiModelProperty(value = "开始销售时间")
    private Date startSoTime;

    /**
     * 结束销售时间
     */
    @ApiModelProperty(value = "结束销售时间")
    private Date endSoTime;

    /**
     * 标识状态：1-正常订单,2-无效订单,3-异常订单
     */
    @ApiModelProperty(value = "标识状态：0-全部 1-正常订单,2-无效订单,3-异常订单")
    private Integer identificationStatus;

    /**
     * 返利状态：1-待返利 2-已返利 3-部分返利
     */
    @ApiModelProperty(value = "返利状态：0-全部 1-待返利 2-已返利 3-部分返利")
    private Integer rebateStatus;



}
