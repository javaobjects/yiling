package com.yiling.user.web.auth.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 员工信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class StaffVO extends BaseVO {

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
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;
}
