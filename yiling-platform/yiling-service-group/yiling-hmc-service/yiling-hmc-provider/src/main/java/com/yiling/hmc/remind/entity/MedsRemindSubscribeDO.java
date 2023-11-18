package com.yiling.hmc.remind.entity;

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
 * 用药提醒消息订阅表
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_meds_remind_subscribe")
public class MedsRemindSubscribeDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 小程序id
     */
    private String appId;

    /**
     * openId
     */
    private String openId;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * 订阅状态 1-正常订阅，2-取消订阅
     */
    private Integer subscribeStatus;

    /**
     * 用户id
     */
    private Long userId;

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
