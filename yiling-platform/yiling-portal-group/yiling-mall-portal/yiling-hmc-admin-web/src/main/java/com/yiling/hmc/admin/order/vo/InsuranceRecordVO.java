package com.yiling.hmc.admin.order.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端参保记录表
 * </p>
 *
 * @author gxl
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
public class InsuranceRecordVO extends BaseVO {

    @ApiModelProperty(value = "保险id")
    private Long insuranceId;

    @ApiModelProperty(value = "保险公司id")
    private Long insuranceCompanyId;

    @ApiModelProperty(value = "平台单号")
    private String orderNo;

    private Integer userId;

    @ApiModelProperty(value = "保司保单号")
    private String policyNo;

    @ApiModelProperty(value = "保单下载地址")
    private String policyUrl;

    @ApiModelProperty(value = "投保单号")
    private String proposalNo;

    @ApiModelProperty(value = "定额方案名称")
    private String comboName;

    @ApiModelProperty(value = "定额方案类型 1-季度，2-年")
    private Integer billType;

    @ApiModelProperty(value = "投保时间")
    private Date proposalTime;

    @ApiModelProperty(value = "保单生效时间")
    private Date effectiveTime;

    @ApiModelProperty(value = "保单终止时间")
    private Date expiredTime;

    @ApiModelProperty(value = "出单时间")
    private Date issueTime;

    @ApiModelProperty(value = "当前终止时间")
    private Date currentEndTime;

    @ApiModelProperty(value = "投保人姓名")
    private String holderName;

    @ApiModelProperty(value = "投保人联系方式")
    private String holderPhone;

    @ApiModelProperty(value = "投保人邮箱")
    private String holderEmail;

    @ApiModelProperty(value = "投保人证件号")
    private String holderCredentialNo;

    @ApiModelProperty(value = "投保人证件类型 01-居民身份证 02-护照 03-军人证 04-驾驶证 05-港澳台同胞证 99-其他")
    private String holderCredentialType;

    @ApiModelProperty(value = "被保人名称")
    private String issueName;

    @ApiModelProperty(value = "被保人联系方式")
    private String issuePhone;

    @ApiModelProperty(value = "被保人邮箱")
    private String issueEmail;

    @ApiModelProperty(value = "被保人证件号")
    private String issueCredentialNo;

    @ApiModelProperty(value = "被保人证件类型")
    private String issueCredentialType;

    @ApiModelProperty(value = "投被保人关系 1：本人 2：配偶 3：子女 4：父母 9：其他")
    private Integer relationType;

    @ApiModelProperty(value = "保险详情id")
    private Integer policyInfoId;

    @ApiModelProperty(value = "销售人员所属eid")
    private Long sellerEid;

    @ApiModelProperty(value = "销售人员id")
    private Long sellerUserId;

    @ApiModelProperty(value = "药店id")
    private Long eid;

    //    @ApiModelProperty(value = "销售员姓名")
    //    private String sellerUserName;


    @ApiModelProperty(value = "保单状态 1-进行中，2-已退保，3-已终止，4-已失效")
    private Integer policyStatus;

    @ApiModelProperty(hidden = true)
    private String idCardFront;

    @ApiModelProperty(hidden = true)
    private String idCardBack;

    @ApiModelProperty(hidden = true)
    private String handSignature;

    @ApiModelProperty(hidden = true)
    private Long createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Long updateUser;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    //    @ApiModelProperty(value = "兑付次数")
    //    private Integer cashTimes;
    //
    //    @ApiModelProperty(value = "累计支付金额")
    //    private BigDecimal totalPayMoney;
    //
    //    @ApiModelProperty(value = "保司")
    //    private String companyName;

}
