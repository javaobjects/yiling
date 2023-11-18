package com.yiling.hmc.insurance.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
public class InsuranceRecordBO implements Serializable {


    private static final long serialVersionUID = 9221293734007443435L;

    private Long id;
    /**
     * 保险id
     */
    private Long insuranceId;

    private String insuranceName;

    /**
     * 用户表主键
     */
    private Integer userId;

    /**
     * 保单号
     */
    private String policyNo;


    /**
     * 平台单号
     */
    private String orderNo;


    /**
     * 投保单号
     */
    private String proposalNo;

    /**
     * 定额方案名称
     */
    private String comboName;

    /**
     * 定额方案类型 1-季度，2-年
     */
    private Integer billType;

    /**
     * 投保时间
     */
    private Date proposalTime;

    /**
     * 保单生效时间
     */
    private Date effectiveTime;

    /**
     * 保单终止时间
     */
    private Date expiredTime;

    /**
     * 出单时间
     */
    private Date issueTime;

    /**
     * 当前终止时间
     */
    private Date currentEndTime;

    /**
     * 投保人姓名
     */
    private String holderName;

    /**
     * 投保人联系方式
     */
    private String holderPhone;

    /**
     * 投保人邮箱
     */
    private String holderEmail;

    /**
     * 投保人证件号
     */
    private String holderCredentialNo;

    /**
     * 投保人证件类型 01-居民身份证 02-护照 03-军人证 04-驾驶证 05-港澳台同胞证 99-其他
     */
    private String holderCredentialType;

    /**
     * 被保人名称
     */
    private String issueName;

    /**
     * 被保人联系方式
     */
    private String issuePhone;

    /**
     * 被保人邮箱
     */
    private String issueEmail;

    /**
     * 被保人证件号
     */
    private String issueCredentialNo;

    /**
     * 被保人证件类型
     */
    private String issueCredentialType;

    /**
     * 投被保人关系 1：本人 2：配偶 3：子女 4：父母 9：其他
     */
    private Integer relationType;

    /**
     * 保险详情id
     */
    private Integer policyInfoId;

    /**
     * 销售人员id
     */
    private Long sellerUserId;

    /**
     * 药店id
     */
    private Long eid;

    /**
     * 销售人员所属eid
     */
    private Long sellerEid;
    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    private Integer policyStatus;


    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 兑付次数
     */
    private Integer cashTimes;

    /**
     * 累计支付金额
     */
    private BigDecimal  totalPayMoney;

    /**
     * 保险公司名称
     */
    private String companyName;


    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;


    /**
     *药品总兑付盒数
     */
    private Long totalCashCount;
    /**
     * 已兑盒数
     */
    private Long cashedTotal;
}
