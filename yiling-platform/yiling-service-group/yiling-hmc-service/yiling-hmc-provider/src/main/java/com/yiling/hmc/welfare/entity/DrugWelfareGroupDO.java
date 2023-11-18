package com.yiling.hmc.welfare.entity;

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
 * 药品福利入组表
 * </p>
 *
 * @author hongyang.zhang
 * @date 2022-09-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_drug_welfare_group")
public class DrugWelfareGroupDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 参与用户id
     */
    private Long userId;

    /**
     * 药品福利id
     */
    private Long drugWelfareId;

    /**
     * 药店id
     */
    private Long eid;

    /**
     * 销售人员id
     */
    private Long sellerUserId;

    /**
     * 用药人姓名
     */
    private String medicineUserName;

    /**
     * 用药人手机号
     */
    private String medicineUserPhone;

    /**
     * 入组id
     */
    private Long joinGroupId;

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
