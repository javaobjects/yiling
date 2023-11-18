package com.yiling.admin.system.system.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 管理员信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AdminVO extends BaseVO {

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 性别：1-男 2-女
     */
    @ApiModelProperty("性别：1-男 2-女")
    private Integer gender;

    /**
     * 出生年月日
     */
    @ApiModelProperty("出生年月日")
    private Date birthday;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 昵称（已废弃）
     */
    @JsonProperty("nickname")
    @ApiModelProperty("昵称（已废弃）")
    private String nickName;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private List<Long> roleIdList;
}
