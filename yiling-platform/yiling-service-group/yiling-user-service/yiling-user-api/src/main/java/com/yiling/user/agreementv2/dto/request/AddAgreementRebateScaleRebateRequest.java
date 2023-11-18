package com.yiling.user.agreementv2.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利-规模返利阶梯 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateScaleRebateRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议时段ID
     */
    private Long segmentId;

    /**
     * 目标达成率
     */
    private BigDecimal targetReachRatio;

    /**
     * 目标达成率单位：1-%
     */
    private Integer reachRatioUnit;

    /**
     * 目标返利率
     */
    private BigDecimal targetRebateRatio;

    /**
     * 目标返利率单位：1-%
     */
    private Integer rebateRatioUnit;

    /**
     * 排序
     */
    private Integer sort;

}
