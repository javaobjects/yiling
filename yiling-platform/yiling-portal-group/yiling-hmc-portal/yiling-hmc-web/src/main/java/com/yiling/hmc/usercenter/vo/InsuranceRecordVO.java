package com.yiling.hmc.usercenter.vo;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 保单记录 VO
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceRecordVO extends BaseVO {

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

    /**
     * 保单终止时间
     */
    @ApiModelProperty("保单终止时间")
    private Date currentEndTime;

    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    @ApiModelProperty("保单状态 1-进行中，2-已退保，3-已终止，4-已失效")
    private Integer policyStatus;

    /**
     * 销售人员编号
     */
    @ApiModelProperty("销售人员编号")
    private String sellerUserNO;

}
