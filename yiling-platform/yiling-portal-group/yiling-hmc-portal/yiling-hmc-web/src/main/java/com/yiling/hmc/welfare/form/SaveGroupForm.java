package com.yiling.hmc.welfare.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 入组
 *
 * @Description
 * @Author fan.shen
 * @Date 2022-09-27
 */
@Data
@ToString
@ApiModel(value = "入组", description = "入组")
@Slf4j
public class SaveGroupForm {

    private static final long serialVersionUID = -7722430332896313642L;

    /**
     * 福利计划id
     */
    @NotNull
    @ApiModelProperty(value = "福利计划id")
    private Long welfareId;

    /**
     * 用药人姓名
     */
    @ApiModelProperty(value = "用药人姓名")
    private String medicineUserName;

    /**
     * 用药人手机号
     */
    @ApiModelProperty(value = "用药人手机号")
    private String medicineUserPhone;

    /**
     * 药店id
     */
    @ApiModelProperty(value = "药店id")
    private Long eid;

    /**
     * 销售人员id
     */
    @ApiModelProperty(value = "销售人员id")
    private Long sellerUserId;

}
