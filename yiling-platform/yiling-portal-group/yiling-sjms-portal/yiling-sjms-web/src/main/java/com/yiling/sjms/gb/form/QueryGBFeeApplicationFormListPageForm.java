package com.yiling.sjms.gb.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 团购列表查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryGBFeeApplicationFormListPageForm extends QueryPageListForm {

    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 团购单位
     */
    @ApiModelProperty(value = "团购单位")
    private String customerName;

    /**
     * 团购出库终端
     */
    @ApiModelProperty(value = "团购出库终端")
    private String termainalCompanyName;

    /**
     * 团购出库商业
     */
    @ApiModelProperty(value = "团购出库商业")
    private String businessCompanyName;

    /**
     * 事业部Id
     */
    @ApiModelProperty(value = "事业部Id")
    private Long orgId;

    /**
     * 团购结束月份
     */
    @ApiModelProperty(value = "团购开始月份")
    private String startMonthTime;


    /**
     * 团购结束月份
     */
    @ApiModelProperty(value = "团购结束月份")
    private String endMonthTime;

    /**
     * 业务类型：1-提报 2-取消
     */
    @ApiModelProperty(value = "所属流程：1-提报 2-取消")
    private Integer bizType;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    @ApiModelProperty(value = "状态：10-待提交 20-审批中 200-已通过 201-已驳回")
    private Integer status;


    /**
     * 提交审批开始时间
     */
    @ApiModelProperty(value = "提交开始时间")
    private Date startSubmitTime;

    /**
     * 提交审批结束时间
     */
    @ApiModelProperty(value = "提交结束时间")
    private Date endSubmitTime;

    /**
     * 审批通过开始时间
     */
    @ApiModelProperty(value = "审批通过开始时间")
    private Date startApproveTime;
    /**
     * 审批通过结束时间
     */
    @ApiModelProperty(value = "审批通过结束时间")
    private Date endApproveTime;

    /**
     * 复核状态 1-未复核 2-已复核
     */
    @ApiModelProperty(value = "复核状态 1-未复核 2-已复核")
    private Integer reviewStatus;

    /**
     * 是否全部团购费用申请数据
     */
    @ApiModelProperty(value = "是否全部团购费用申请数据 1-全部 2-发起人自己")
    @NotNull
    private Integer allData;
}
