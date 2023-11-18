package com.yiling.marketing.promotion.entity;

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
 * 专场活动预约表
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_special_appointment")
public class PromotionSpecialAppointmentDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 专场活动表id
     */
    private Long specialActivityId;

    /**
     * 专场活动企业表id
     */
    private Long specialActivityEnterpriseId;

    /**
     * 预约时间
     */
    private Date appointmentTime;

    /**
     * 预约人id
     */
    private Long appointmentUserId;

    /**
     * 预约人姓名
     */
    private String appointmentUserName;

    /**
     * 预约人所属企业id
     */
    private Long appointmentUserEid;

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

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
