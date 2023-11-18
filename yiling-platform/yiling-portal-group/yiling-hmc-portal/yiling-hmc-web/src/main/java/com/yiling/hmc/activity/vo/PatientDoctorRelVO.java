package com.yiling.hmc.activity.vo;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.ih.user.dto.PatientDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 医患关系 VO
 *
 * @author: fan.shen
 * @date: 2022-09-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PatientDoctorRelVO extends BaseVO {

    /**
     * 是否加入 true - 是，false - 否
     */
    @ApiModelProperty(value = "是否加入 true - 是，false - 否")
    private Boolean hasJoin;

    /**
     * 患者名称
     */
    @ApiModelProperty(value = "患者名称")
    private String patientName;

    /**
     * 患者年龄
     */
    @ApiModelProperty(value = "患者年龄")
    private Integer patientAge;

    /**
     * 患者性别
     */
    @ApiModelProperty(value = "患者性别 1-男，0-女 ")
    private Integer gender;

}
