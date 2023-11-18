package com.yiling.hmc.admin.insurance.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 交易记录-详情-保单信息tab
 * @author gxl
 */
@Data
@Accessors(chain = true)
public class InsuranceRecordDetailVO {


    @ApiModelProperty("保单信息")
    private InsuranceRecordVO insuranceRecord;

    @ApiModelProperty("拿药计划")
    private List<InsuranceFetchPlanVO> fetchPlanList;

    @ApiModelProperty("拿药计划详情-每个拿药计划下的都一样")
    private List<InsuranceFetchPlanDetailVO> fetchPlanDetailList;

    @ApiModelProperty("总计")
    private BigDecimal total;
}
