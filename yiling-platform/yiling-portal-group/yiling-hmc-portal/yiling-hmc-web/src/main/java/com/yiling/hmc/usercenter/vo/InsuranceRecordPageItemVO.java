package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 我的参保 VO
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceRecordPageItemVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    /**
     * 保单号
     */
    @ApiModelProperty("保单号")
    private String policyNo;

    /**
     * 被保人名称
     */
    @ApiModelProperty("被保人名称")
    private String issueName;

    /**
     * 被保人联系方式
     */
    @ApiModelProperty("被保人联系方式")
    private String issuePhone;

    /**
     * 被保人证件号
     */
    @ApiModelProperty("被保人证件号")
    private String issueCredentialNo;

    /**
     * 投保时间
     */
    @ApiModelProperty("投保时间")
    private Date proposalTime;

}
