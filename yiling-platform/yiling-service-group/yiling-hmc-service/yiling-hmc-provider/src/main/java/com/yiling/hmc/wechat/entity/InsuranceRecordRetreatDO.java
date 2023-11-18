package com.yiling.hmc.wechat.entity;

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
 * 退保记录表
 * </p>
 *
 * @author fan.shen
 * @date 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_record_retreat")
public class InsuranceRecordRetreatDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 参保记录表id
     */
    private Long insuranceRecordId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 保单号
     */
    private String policyNo;

    /**
     * 01 - 身份证
     */
    private Integer idType;

    /**
     * 身份证号
     */
    private String idNo;

    /**
     * 退费金额 单位：分
     */
    private Long retMoney;

    /**
     * 退保时间 yyyy-MM-dd HH:mm:ss
     */
    private Date retTime;

    /**
     * 承保保费 单位：分
     */
    private Integer premium;

    /**
     * 分期数量
     */
    private String installmentsTotal;

    /**
     * 保单状态类型 15-是保单注销,16 - 是保单退保,18 - 是保险合同终止,71 - 保单失效
     */
    private Integer endPolicyType;

    /**
     * 定额方案
     */
    private String flowId;

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
