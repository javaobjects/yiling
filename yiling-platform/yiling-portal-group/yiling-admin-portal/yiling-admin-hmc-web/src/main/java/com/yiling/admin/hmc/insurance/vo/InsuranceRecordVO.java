package com.yiling.admin.hmc.insurance.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper=true)
public class InsuranceRecordVO extends BaseVO {

    /**
     * 保险id
     */
    @ApiModelProperty(value = "保险id")
    private Long insuranceId;

    @ApiModelProperty(value = "保险名称")
    private String insuranceName;

    /**
     * 定额方案名称
     */
    @ApiModelProperty(value = "定额方案名称")
    private String comboName;

    /**
     * 电子保单下载地址
     */
    @ApiModelProperty(value = "电子保单下载地址")
    private String policyUrl;

    /**
     * 用户表主键
     */
    @ApiModelProperty(hidden = true)
    private Integer userId;

    /**
     * 保单号
     */
    @ApiModelProperty(value = "保司保单号")
    private String policyNo;

    /**
     * 销售人员所属eid
     */
    @ApiModelProperty(hidden = true)
    private Long sellerEid;

    /**
     * 销售人员所属企业
     */
    @ApiModelProperty("销售人员所属企业")
    private String sellerEName;

    /**
     * 销售人员编号
     */
    @ApiModelProperty("销售人员编号")
    private String sellerUserNO;

    /**
     * 平台单号
     */
    @ApiModelProperty(value = "平台单号")
    private String orderNo;

    /**
     * 定额方案类型 1-季度，2-年
     */
    @ApiModelProperty(value = "定额方案类型 1-季度，2-年")
    private Integer billType;

    /**
     * 投保时间
     */
    @ApiModelProperty(value = "投保时间")
    private Date proposalTime;

    /**
     * 投保人姓名
     */
    @ApiModelProperty(value = "投保人姓名")
    private String holderName;

    /**
     * 投保人联系方式
     */
    @ApiModelProperty(value = "投保人联系方式")
    private String holderPhone;

    /**
     * 投保人证件号
     */
    @ApiModelProperty(value = "投保人证件号")
    private String holderCredentialNo;

    /**
     * 被保人名称
     */
    @ApiModelProperty(value = "被保人名称")
    private String issueName;

    /**
     * 被保人联系方式
     */
    @ApiModelProperty(value = "被保人联系方式")
    private String issuePhone;

    /**
     * 被保人证件号
     */
    @ApiModelProperty("被保人证件号")
    private String issueCredentialNo;

    /**
     * 保险详情id
     */
    @ApiModelProperty(value = "保险详情id")
    private Integer policyInfoId;

    /**
     * 销售人员id
     */
    @ApiModelProperty(value = "销售人员id 来源终端类型： 等于0代表线上 大于0代表线下")
    private Long sellerUserId;

    @ApiModelProperty(value = "销售员姓名")
    private String sellerUserName;
    /**
     * 药店id
     */
    @ApiModelProperty(hidden = true)
    private Long eid;

    /**
     * 药店名称
     */
    @ApiModelProperty(value = "药店名称（保险兑付药店）")
    private String ename;

    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    @ApiModelProperty(value = "保单状态 1-进行中，2-已退保，3-已终止，4-已失效")
    private Integer policyStatus;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(hidden = true)
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 兑付次数
     */
    @ApiModelProperty(value = "兑付次数")
    private Integer cashTimes;

    /**
     * 累计支付金额
     */
    @ApiModelProperty(value = "累计支付金额")
    private BigDecimal  totalPayMoney;

    @ApiModelProperty(value = "保司")
    private String companyName;

    /**
     * 保单生效时间
     */
    @ApiModelProperty(value = "保单生效时间")
    private Date effectiveTime;

    /**
     * 保单终止时间
     */
    @ApiModelProperty(value = "保单终止时间")
    private Date currentEndTime;

}
