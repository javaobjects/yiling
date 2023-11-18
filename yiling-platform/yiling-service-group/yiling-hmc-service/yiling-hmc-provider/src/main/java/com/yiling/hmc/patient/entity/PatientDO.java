package com.yiling.hmc.patient.entity;

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
 * 就诊人
 * </p>
 *
 * @author gxl
 * @date 2022-04-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_patient")
public class PatientDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 患者名称
     */
    private String patientName;

    /**
     * 患者关联的用户id
     */
    private Long fromUserId;

    /**
     * 被保人身份证
     */
    private String idCard;

    /**
     * 被保人手机号
     */
    private String mobile;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 患者性别 1男 0女
     */
    private Integer gender;

    /**
     * 省code
     */
    private String provinceCode;

    /**
     * 市code
     */
    private String cityCode;

    /**
     * 区code
     */
    private String regionCode;

    /**
     * 详细地址
     */
    private String address;

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
