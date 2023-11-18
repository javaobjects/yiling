package com.yiling.user.system.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建健康管理中心用户 Request
 *
 * @author: xuan.zhou
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateHmcUserRequest extends BaseRequest {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 头像路径
     */
    private String avatarUrl;

    /**
     * openId
     */
    @NotEmpty
    private String openId;

    /**
     * appId
     */
    private String appId;

    /**
     * unionId
     */
    @NotEmpty
    private String unionId;

    /**
     * 注册来源：1-自然流量 2-店员或销售 3-扫药盒二维码 4-医生推荐
     */
    @NotNull
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
     * C端活动id
     */
    private Long activityId;

    /**
     * 活动来源
     */
    private Integer activitySource;

}
