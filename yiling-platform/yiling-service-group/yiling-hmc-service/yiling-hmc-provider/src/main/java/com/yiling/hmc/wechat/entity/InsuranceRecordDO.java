package com.yiling.hmc.wechat.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端参保记录表
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_record")
public class InsuranceRecordDO extends BaseDO {

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
     * 平台订单编号
     */
    private String orderNo;

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
     * 保单状态 1-进行中，2-已退保，3-已终止，4-已失效
     */
    private Integer policyStatus;

    /**
     * 身份证正面
     */
    private String idCardFront;

    /**
     * 身份证背面
     */
    private String idCardBack;

    /**
     * 手写签名
     */
    private String handSignature;

    /**
     * 是否删除 0-否，1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
