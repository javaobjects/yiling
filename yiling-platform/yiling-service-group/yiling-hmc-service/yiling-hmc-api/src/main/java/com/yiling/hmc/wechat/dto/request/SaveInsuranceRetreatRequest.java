package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.hmc.wechat.enums.HmcPolicyEndTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 创建退保记录
 *
 * @author fan.shen
 * @date 2022/4/6
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveInsuranceRetreatRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 01 - 身份证
     */
    private String idType;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 退费金额 单位：分
     */
    private Long retMoney;

    /**
     * 退保时间 yyyy-MM-dd HH:mm:ss
     */
    private Date retTime;

    /**
     * 承保保费 单位：分
     */
    private Integer premium;

    /**
     * 分期数量
     */
    private String installmentsTotal;

    /**
     * 保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效
     */
    private Integer endPolicyType;

    /**
     * 定额方案
     */
    private String flowId;

    /**
     * 保单终止类型
     */
    private HmcPolicyEndTypeEnum match;

}
