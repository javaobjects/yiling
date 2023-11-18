package com.yiling.admin.hmc.user.vo;

import java.util.Date;

import com.yiling.hmc.wechat.enums.SourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 健康管理中心用户信息
 *
 * @author: xuan.zhou
 * @date: 2022/3/24
 */
@Data
public class HmcUserVO {

    /**
     * 用户ID
     */
    private Long id;

    @ApiModelProperty(value = "昵称或者姓名")
    private String name;

    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像")
    private String avatarUrl;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;


    /**
     * @see SourceEnum
     */
    @ApiModelProperty(value = "注册来源：1-自然流量 2-店员或销售 3-扫药盒二维码 4-医生推荐 5-用户推荐 6- 以岭互联网医院 ")
    private Integer registerSource;

    /**
     * unionId
     */
    @ApiModelProperty(value = "unionId")
    private String unionId;

    /**
     * 邀请人id
     */
    private Long inviteUserId;

    @ApiModelProperty(value = "邀请人姓名")
    private String inviteUserName;

    @ApiModelProperty(value = "邀请人所在企业")
    private String inviteEname;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别：0-未知 1-男 2-女")
    private Integer gender;

    /**
     * 登录次数
     */
    @ApiModelProperty(value = "登录次数")
    private Integer loginCount;


    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "注册时间")
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
    @ApiModelProperty(value = "关联就诊人数")
    private Long patientCount;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "活动名称")
    private String activityName;

    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    private Long activityId;

}
