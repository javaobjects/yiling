package com.yiling.dataflow.wash.entity;

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
 * 流向日程表
 * </p>
 *
 * @author baifc
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_month_wash_control")
public class FlowMonthWashControlDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 数据开始时间
     */
    private Date dataStartTime;

    /**
     * 数据结束时间
     */
    private Date dataEndTime;
    /**
     * 流向上传、商品对照开始时间
     */
    private Date goodsMappingStartTime;

    /**
     * 流向上传、商品对照结束时间
     */
    private Date goodsMappingEndTime;

    /**
     * 客户对照、销量申诉开始时间
     */
    private Date customerMappingStartTime;

    /**
     * 客户对照、销量申诉结束时间
     */
    private Date customerMappingEndTime;

    /**
     * 在途库存、终端库存上报开始时间
     */
    private Date goodsBatchStartTime;

    /**
     * 在途库存、终端库存上报结束时间
     */
    private Date goodsBatchEndTime;

    /**
     * 窜货提报开始时间
     */
    private Date flowCrossStartTime;

    /**
     * 窜货提报结束时间
     */
    private Date flowCrossEndTime;

    /**
     * 团购开始时间
     */
    private Date flowGroupStartTime;

    /**
     * 团购结束时间
     */
    private Date flowGroupEndTime;

    private Integer employeeBackupStatus;
    private Date    employeeBackupTime;
    private Integer basisStatus;
    private Date    basisTime;
    private Integer basisBackupStatus;
    private Date    basisBackupTime;
    private Integer washStatus;
    private Date    washTime;
    private Integer gbLockStatus;
    private Date    gbLockTime;
    private Integer unlockStatus;
    private Date    unlockTime;
    private Integer gbUnlockStatus;
    private Date    gbUnlockTime;
    private Integer taskStatus;
    private Date    taskTime;

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
