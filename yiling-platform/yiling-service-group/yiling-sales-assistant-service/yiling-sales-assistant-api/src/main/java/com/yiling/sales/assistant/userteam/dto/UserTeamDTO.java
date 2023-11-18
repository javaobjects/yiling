package com.yiling.sales.assistant.userteam.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手-团队管理DTO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserTeamDTO extends BaseDTO {

    /**
     * 队长ID（队员的时候才有值）
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
    private Long mobilePhone;
    /**
     * 是否为队长：0-否 1-是
     */
    private Integer leaderStatus;

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
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

}
