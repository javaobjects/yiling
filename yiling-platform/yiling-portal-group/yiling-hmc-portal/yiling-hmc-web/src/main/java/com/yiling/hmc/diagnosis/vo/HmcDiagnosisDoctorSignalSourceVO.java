package com.yiling.hmc.diagnosis.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * HMC获取医生号源列表VO
 * @author: fan.shen
 * @data: 2023/05/09
 */
@Data
public class HmcDiagnosisDoctorSignalSourceVO {

    @ApiModelProperty("日期")
    private String dateTime;

    @ApiModelProperty("精确日期")
    private Date date;

    @ApiModelProperty("所属时段集合")
    private List<HmcDoctorSignalSourceBelongTimeVO> belongTimeList;
}
