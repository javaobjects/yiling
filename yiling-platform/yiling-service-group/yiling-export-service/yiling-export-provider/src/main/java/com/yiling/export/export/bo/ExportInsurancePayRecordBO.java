package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 交易记录导出
 * </p>
 *
 * @author gxl
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
public class ExportInsurancePayRecordBO {


    private Long id;
    /**
     * 保险id
     */
    private Long insuranceId;

    private String insuranceName;

    private String cashTerminalName;

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
    private String billTypeName;

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
     * 被保人名称
     */
    private String issueName;
    /**
     * 被保人联系方式
     */
    private String issuePhone;

    /**
     * 被保人证件号
     */
    private String issueCredentialNo;

    /**
     * 销售人员姓名
     */
    private String sellerUserName;
    /**
     * 销售人员id
     */
    private Long sellerUserId;

    /**
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    private String policyStatus;

    /**
     * 兑付次数
     */
    private Integer cashTimes = 0;

    /**
     * 实收金额
     */
    private BigDecimal  amount = BigDecimal.ZERO;

    /**
     * 保险公司名称
     */
    private String companyName;

    /**
     * 终端名称
     */
    private String terminalName;
    /**
     * 来源类型
     */
    private String sourceTypeName;
    /**
     * 销售员电话
     */
    private String sellerPhone;


    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     *药品总兑付盒数
     */
    private Integer totalCashCount = 0;
    /**
     * 已兑盒数
     */
    private Integer cashedTotal = 0;

    /**
     * 销售人员所属eid
     */
    private Long sellerEid;
}
