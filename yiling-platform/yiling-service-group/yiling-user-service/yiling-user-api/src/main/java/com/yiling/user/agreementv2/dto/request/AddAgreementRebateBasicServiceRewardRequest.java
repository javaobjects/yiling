package com.yiling.user.agreementv2.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利-基础服务奖励阶梯 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateBasicServiceRewardRequest extends BaseRequest {

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议时段ID
     */
    private Long segmentId;

    /**
     * 增长率
     */
    private BigDecimal increaseRatio;

    /**
     * 增长率单位：1-% 2-元 3-盒
     */
    private Integer increaseRatioUnit;

    /**
     * 返利
     */
    private BigDecimal rebateNum;

    /**
     * 返利单位：1-% 2-元 3-盒
     */
    private Integer rebateNumUnit;

    /**
     * 排序
     */
    private Integer sort;

}
