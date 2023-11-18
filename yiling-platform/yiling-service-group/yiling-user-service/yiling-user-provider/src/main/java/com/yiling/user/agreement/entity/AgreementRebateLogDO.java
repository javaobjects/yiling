package com.yiling.user.agreement.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 协议兑付日志表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-07-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agreement_rebate_log")
public class AgreementRebateLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 兑付者的easCode
     */
    private String easCode;

    /**
     * 日志记录名称
     */
    private String logName;

    /**
     * 协议id
     */
    private Long agreementId;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 兑付类型：1-协议兑付 2-其他
     */
    private Integer cashType;

    /**
     * 兑付金额
     */
    private BigDecimal discountAmount;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
