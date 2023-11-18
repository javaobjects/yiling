package com.yiling.hmc.insurance.bo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 泰康保险试算对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsuranceTrialBO {

    /**
     * 是否定向赔付
     */
    private Boolean hasPayInfo;

    /**
     * 是否包含处方
     */
    private Boolean hasRecipel;

    /**
     * 服务入口渠道编码 泰康提供
     */
    private String sourceChannelCode;

    /**
     * 服务入口渠道名称 泰康提供
     */
    private String sourceChannelName;

    /**
     * 产品编码 泰康提供
     */
    private String productCode;

    /**
     * 子保单号 渠道传入泰康保单号
     */
    private String policyNo;

    /**
     * 渠道服务订单号
     */
    private String channelMainId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

    /**
     * 申请人电话
     */
    private String applyUserPhone;

    /**
     * 申请人证件号
     */
    private String applyUserCid;

    /**
     * 申请人证件类型
     */
    private Integer applyUserCidType = 1;

    /**
     * 用药人姓名
     */
    private String patientName;

    /**
     * 用药人生日
     */
    private Date patientBirth;

    /**
     * 用药人证件号
     */
    private String patientCid;

    /**
     * 用药人证件类型
     */
    private Integer patientCidType = 1;

    /**
     * 用药人性别 0未知 1男性 2女性
     */
    private String patientGender;

    /**
     * 用药人手机号
     */
    private String patientPhone;

    /**
     * 购药类型 4-福利购药
     */
    private Integer pharmacyType;

    /**
     * 就诊类型
     * 1-线上就诊
     * 2-线下就诊
     */
    private Integer consultationType = 2;

    /**
     * 服务订单状态 0-新订单
     */
    private Integer status = 0;

    /**
     * 服务单总价
     */
    private String marketPrices;

    /**
     * 下单时间 格式yyyy-MM-dd HH:mm:ss
     */
    private String applyTime;

    /**
     * 购药订单集合
     */
    private List<OrderDetail> orderList;

    /**
     * 处方信息
     */
    private TkRecipelInfoBO recipelInfo;

}
