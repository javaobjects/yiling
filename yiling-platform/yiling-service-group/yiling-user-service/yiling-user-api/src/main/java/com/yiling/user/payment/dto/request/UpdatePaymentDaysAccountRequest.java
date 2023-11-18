package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/5/24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class UpdatePaymentDaysAccountRequest extends BaseRequest {

    private static final long serialVersionUID = -6098751152745527004L;

    /**
     * ID
     */
    private Long id;

    /**
     * 修改后
     */
    private BigDecimal totalAmount;

    /**
     * 是否长期有效：0-否 1-是
     */
    private Integer longEffective;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 还款周期
     */
    private Integer period;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 账期上浮点位（百分比）
     */
    private BigDecimal upPoint;

    /**
     * 平台类型
     */
    private PlatformEnum platformType;


}
