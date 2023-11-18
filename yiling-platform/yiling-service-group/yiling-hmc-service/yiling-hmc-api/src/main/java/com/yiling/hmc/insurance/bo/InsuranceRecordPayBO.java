package com.yiling.hmc.insurance.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端缴费记录表
 * </p>
 *
 * @author gxl
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
public class InsuranceRecordPayBO implements Serializable {


    private static final long serialVersionUID = -1108924577122824601L;
    private Long id;
    /**
     * 保险id
     */
    private Long insuranceId;

    private Long insuranceRecordId;

    private String insuranceName;


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
     * 被保人名称
     */
    private String issueName;

    /**
     * 被保人联系方式
     */
    private String issuePhone;



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
     * 实收金额
     */
    private BigDecimal amount;
    /**
     * 转换前的金额
     */
    private Long preAmount;
    /**
     * 保险公司名称
     */
    private String companyName;


    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 销售人员所属eid
     */
    private Long sellerEid;


    /**
     *药品总兑付盒数
     */
    private Long totalCashCount;
    /**
     * 已兑盒数
     */
    private Long cashedTotal;
}
