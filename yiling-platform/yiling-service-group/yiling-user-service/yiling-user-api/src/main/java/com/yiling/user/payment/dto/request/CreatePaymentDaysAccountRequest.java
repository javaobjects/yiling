package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.enums.PlatformEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户账期账户表
 * </p>
 *
 * @author gxl
 * @date 2021-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreatePaymentDaysAccountRequest extends BaseDO {

    private static final long serialVersionUID = -2939592934445565466L;

    /**
     * 企业id（供应商）
     */
    private Long eid;

    /**
     * 客户id（采购商）
     */
    private Long customerEid;

    /**
     * 账期额度
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
     * 账户类型：1-以岭 2-非以岭 3-工业直属
     */
    private Integer type;

    /**
     * 账期上浮点位（百分比）
     */
    private BigDecimal upPoint;

    /**
     * 平台类型
     */
    private PlatformEnum platformType;

}
