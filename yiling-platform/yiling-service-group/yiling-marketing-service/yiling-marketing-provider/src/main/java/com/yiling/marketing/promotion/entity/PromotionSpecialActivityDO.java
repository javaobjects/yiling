package com.yiling.marketing.promotion.entity;

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
 * 专场活动主表
 * </p>
 *
 * @author shixing.sun
 * @date 2022-05-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_promotion_special_activity")
public class PromotionSpecialActivityDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 专场活动名称
     */
    private String specialActivityName;

    /**
     * 专场活动开始时间
     */
    private Date startTime;

    /**
     * 专场活动结束时间
     */
    private Date endTime;

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
     * 活动类型（1-满赠,2-特价,3-秒杀,4-组合包）
     */
    private Integer type;

    /**
     * 活动状态（1-启用；2-停用；）
     */
    private Integer status;

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
