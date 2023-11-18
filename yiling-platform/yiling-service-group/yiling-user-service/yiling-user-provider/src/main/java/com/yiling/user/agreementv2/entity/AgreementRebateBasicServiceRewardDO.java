package com.yiling.user.agreementv2.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议返利-基础服务奖励阶梯表
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_basic_service_reward")
public class AgreementRebateBasicServiceRewardDO extends BaseDO {

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

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;


}
