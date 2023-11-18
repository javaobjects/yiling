package com.yiling.hmc.admin.insurance.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author gxl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class QueryInsuranceRecordPayPageForm extends QueryPageListForm {

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    @ApiModelProperty(value = "交费时间-开始")
    private Date startPayTime;

    @ApiModelProperty(value = "交费时间-结束")
    private Date endPayTime;


    @ApiModelProperty(value = "销售员id")
    private Long sellerUserId;

    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    @ApiModelProperty(value = "保单状态 1-进行中，2-已退保，3-已终止，4-已失效")
    private Integer policyStatus;

    /**
     * 保司单号
     */
    @ApiModelProperty(value = "保司单号")
    private String policyNo;
    /**
     * 被报人姓名
     */
    @ApiModelProperty(value = "被报人姓名")
    private String issueName;

    @ApiModelProperty(value = "投保人姓名")
    private String holderName;
    
    @ApiModelProperty(value = "投/被保人手机号")
    private String issuePhone;
}
