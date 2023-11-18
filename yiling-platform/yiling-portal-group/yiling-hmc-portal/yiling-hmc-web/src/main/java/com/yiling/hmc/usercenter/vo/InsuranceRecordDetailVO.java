package com.yiling.hmc.usercenter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 保单详情 VO
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@ApiModel
@Accessors(chain = true)
public class InsuranceRecordDetailVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单来源")
    private String orderSource;

    @ApiModelProperty("保险名称")
    private String insuranceName;

    @ApiModelProperty("保司名称")
    private String insuranceCompanyName;

    @ApiModelProperty("退保url")
    private String retreatUrl;

    @ApiModelProperty("续保url")
    private String reNewUrl;

    @ApiModelProperty("待确认兑付的订单")
    private OrderVO processingOrder;

    @ApiModelProperty("是否有过兑付订单 true-是，false-否")
    private Boolean hasOrder;

    @ApiModelProperty("是否有续费计划 true-是，false-否")
    private Boolean hasPayPlan;

    @ApiModelProperty("下次拿药时间")
    private Date nextFetchTime;

    @ApiModelProperty("还剩N次")
    private Long leftTimes;

    @ApiModelProperty("退保客服电话")
    private String retreatTelephone;

    @ApiModelProperty("互联网问诊地址")
    private String internetConsultationUrl;

    @ApiModelProperty("保单信息")
    private InsuranceRecordVO insuranceRecord;

    @ApiModelProperty("拿药计划")
    private List<InsuranceFetchPlanVO> fetchPlanList;

    @ApiModelProperty("拿药计划详情")
    private List<InsuranceFetchPlanDetailVO> fetchPlanDetailList;

}
