package com.yiling.hmc.wechat.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 公众号用户信息DTO
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceRecordPayPlanDTO extends BaseDTO {

    /**
     * 参保记录表id
     */
    private Integer insuranceRecordId;

    /**
     * 缴费金额
     */
    private BigDecimal premium;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 缴费期次
     */
    private Integer paySequence;

}
