package com.yiling.hmc.insurance.bo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
public class TkOrderResult implements Serializable {

    /**
     * 理算结果唯一标识
     */
    private String claimNo;

    /**
     * 子保单号 保单号
     */
    private String policyNo;

    /**
     * 团单号  团单号
     */
    private String multipleNo;

    /**
     * 渠道服务订单号
     */
    private String channelMainId;

    /**
     * 理算结果 0-成功；1-保单不存在；2-保单已失效；3-药品目录校验未通过；
     * 4-药品购药限制校验未通过（超过购药次数或购药金额等的限制）；5-处方诊断结果中的疾病不再疾病目录中；6-批准文号错误（西药）；
     * 7-存在除外药品（中药）；8-服务使用人与被保人不一致;9-账户金锁定失败；
     * 99-失败。不会控制的校验，试算理算就不会出现相应的结果编码
     */
    private Integer claimResultCode;

    /**
     * 理算结果说明   理算结果说明，如单次购药金额校验不通过等...
     */
    private String claimResultMsg;

    /**
     * 订单总金额    调用接口时传入的总金额
     */
    private BigDecimal totalAmount;

    /**
     * 保险报销金额   单位元  抵扣金额/保险报销金额
     */
    private BigDecimal reimbursementAmount;

    /**
     * 理赔金额     单位元 实际理赔金额（调用理赔的时候需要）
     */
    private BigDecimal claimAmount;
}
