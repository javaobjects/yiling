package com.yiling.sales.assistant.userteam.entity;

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
 * 销售助手-用户团队关系DO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_user_team")
public class UserTeamDO extends BaseDO {

    private static final long serialVersionUID = 4434960345586799096L;

    /**
     * 队长ID
     */
    private Long parentId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 成员名称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 注册状态：0-未注册 1-已注册
     */
    private Integer registerStatus;

    /**
     * 邀请方式：1-短信 2-微信
     */
    private Integer inviteType;

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
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

}
