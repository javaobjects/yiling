package com.yiling.user.agreementv2.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 添加协议返利-项目服务奖励阶梯 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddAgreementRebateProjectServiceRewardRequest extends BaseRequest {


    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议时段ID
     */
    private Long segmentId;

    /**
     * 覆盖率
     */
    private BigDecimal coverRatio;

    /**
     * 覆盖率单位：1-% 2-元 3-盒
     */
    private Integer coverRatioUnit;

    /**
     * 其它
     */
    private BigDecimal other;

    /**
     * 其它单位：1-% 2-元 3-盒
     */
    private Integer otherUnit;

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
