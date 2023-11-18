package com.yiling.hmc.order.entity;

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
 * C端兑付订单关联处方表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_order_prescription")
public class OrderPrescriptionDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 开方日期
     */
    private Date receiptDate;

    /**
     * 开方医生
     */
    private String doctor;

    /**
     * 处方编号
     */
    private String prescriptionNo;

    /**
     * 患者姓名
     */
    private String patientsName;

    /**
     * 患者性别
     */
    private String patientsGender;

    /**
     * 患者年龄
     */
    private Integer patientsAge;

    /**
     * 问诊科室
     */
    private String interrogationRoom;

    /**
     * 症状描述
     */
    private String symptomsDesc;

    /**
     * 诊断结果
     */
    private String interrogationResult;

    /**
     * 处方快照url
     */
    private String prescriptionSnapshotUrl;

    /**
     * 补充描述
     */
    private String addDesc;

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;


}
