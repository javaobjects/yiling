package com.yiling.user.integral.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单送积分倍数配置表 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IntegralOrderGiveMultipleConfigDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 发放规则ID
     */
    private Long giveRuleId;

    /**
     * 指定客户范围的用户类型：1-全部用户 2-普通用户 3-全部会员 4-部分会员（部分会员具体到会员方案上）
     */
    private Integer userType;

    /**
     * 部分会员时的会员ID
     */
    private Long memberId;

    /**
     * 支付方式：1-线上支付 2-线下支付 3-账期支付
     */
    private Integer paymentMethod;

    /**
     * 积分倍数
     */
    private BigDecimal integralMultiple;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
