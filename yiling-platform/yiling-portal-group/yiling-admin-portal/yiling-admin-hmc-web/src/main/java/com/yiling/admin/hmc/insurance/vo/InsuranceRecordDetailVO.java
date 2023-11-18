package com.yiling.admin.hmc.insurance.vo;

import com.yiling.hmc.order.enums.HmcSourceTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 保单纬度详情 VO
 *
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@ApiModel
@Accessors(chain = true)
public class InsuranceRecordDetailVO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("保险名称")
    private String insuranceName;

    @ApiModelProperty("保司名称")
    private String insuranceCompanyName;

    @ApiModelProperty(value = "来源类型 0-线上渠道；1-线下渠道")
    private Integer sourceType = HmcSourceTypeEnum.ONLINE.getCode();

    @ApiModelProperty("保单信息")
    private InsuranceRecordVO insuranceRecord;

    @ApiModelProperty("拿药计划")
    private List<InsuranceFetchPlanVO> fetchPlanList;

    @ApiModelProperty("拿药计划详情")
    private List<InsuranceFetchPlanDetailVO> fetchPlanDetailList;

    @ApiModelProperty("保单交费记录")
    private List<InsuranceRecordPayHisVO> payHisList;

    @ApiModelProperty("当前所交期")
    private Integer currentPayStage;

}
