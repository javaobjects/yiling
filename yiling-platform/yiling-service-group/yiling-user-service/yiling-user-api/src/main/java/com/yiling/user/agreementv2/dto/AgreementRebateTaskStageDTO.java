package com.yiling.user.agreementv2.dto;

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
 * 协议返利任务量阶梯 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgreementRebateTaskStageDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 协议ID
     */
    private Long agreementId;

    /**
     * 协议时段ID
     */
    private Long segmentId;

    /**
     * 商品组ID
     */
    private Long groupId;

    /**
     * 协议返利范围ID
     */
    private Long rebateScopeId;

    /**
     * 任务量
     */
    private BigDecimal taskNum;

    /**
     * 任务量单位：1-元 2-盒
     */
    private Integer taskUnit;

    /**
     * 超任务量汇总返
     */
    private BigDecimal overSumBack;

    /**
     * 超任务量汇总返单位：1-元 2-%
     */
    private Integer overSumBackUnit;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
