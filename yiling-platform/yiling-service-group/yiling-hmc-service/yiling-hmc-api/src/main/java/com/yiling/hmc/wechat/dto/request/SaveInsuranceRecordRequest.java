package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 创建参保记录
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveInsuranceRecordRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 保险id
     */
    private Long insuranceId;

    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;

    /**
     * 用户表主键
     */
    private Long userId;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 保单下载地址
     */
    private String policyUrl;

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
     * 销售人员所属eid
     */
    private Long sellerEid;

    /**
     * 销售人员id
     */
    private Long sellerUserId;

    /**
     * 药店id
     */
    private Long eid;

    /**
     * 保单状态 10-进行中，20-已退保
     */
    private Integer policyStatus;


}
