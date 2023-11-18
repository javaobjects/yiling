package com.yiling.admin.sales.assistant.userteam.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手后台-团队成员列表VO
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/29
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserTeamListItemVO extends BaseVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 邀请人用户ID
     */
    @ApiModelProperty("邀请人用户ID")
    private Long parentId;

    /**
     * 成员名称
     */
    @ApiModelProperty("成员名称")
    private String name;

    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private Date registerTime;

    /**
     * 注册状态：0-未注册 1-已注册 9-已注销
     */
    @ApiModelProperty("注册状态：0-未注册 1-已注册 9-已注销")
    private Integer registerStatus;

    /**
     * 注销时间
     */
    @ApiModelProperty("注销时间")
    private Date deregisterTime;

    /**
     * 邀请方式：1-短信 2-微信
     */
    @ApiModelProperty("邀请方式：1-短信 2-微信")
    private Integer inviteType;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobilePhone;

    /**
     * 拉取人
     */
    @ApiModelProperty("拉取人")
    private String inviteName;

    /**
     * 身份证
     */
    @ApiModelProperty("身份证")
    private String idNumber;

    /**
     * 职务
     */
    @ApiModelProperty("职务")
    private String position;

    /**
     * 所属团队数量
     */
    @ApiModelProperty("所属团队数量")
    private Integer teamNum;

    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;


}
