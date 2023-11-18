package com.yiling.hmc.activity.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端医带患活动
 * </p>
 *
 * @author fan.shen
 * @date 2023-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_activity_doc_to_patient")
public class ActivityDocToPatientDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * C端活动id
     */
    private Long activityId;

    /**
     * 限制医生类型：1-手动配置
     */
    private Integer restrictDocType;

    /**
     * 限制用户类型：1-平台新用户（没有注册过的），2-不限制
     */
    private Integer restrictUserType;

    /**
     * 审核用户类型：1-需要审核，2-无需审核
     */
    private Integer auditUserType;

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
