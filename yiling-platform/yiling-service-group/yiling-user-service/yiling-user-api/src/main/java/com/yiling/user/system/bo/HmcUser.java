package com.yiling.user.system.bo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 健康管理中心用户信息
 *
 * @author: xuan.zhou
 * @date: 2022/3/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HmcUser extends BaseUser {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 健康管理中心小程序appId
     * @TODO 后期需要移走
     */
    private String appId;

    /**
     * 健康管理中心小程序openId
     * @TODO 后期需要移走
     */
    private String miniProgramOpenId;

    /**
     * unionId
     */
    private String unionId;

    /**
     * 注册来源：1-自然流量 2-店员或销售 3-扫药盒二维码 4-医生推荐 5-用户推荐 6-以岭互联网医院
     */
    private Integer registerSource;

    /**
     * 邀请人id
     */
    private Long inviteUserId;

    /**
     * 邀请人企业id
     */
    private Long inviteEid;

    /**
     * 活动id
     */
    private Long activityId;

    /**
     * 活动来源
     */
    private Integer activitySource;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
