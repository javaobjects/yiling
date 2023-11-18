package com.yiling.admin.data.center.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserVO extends BaseVO {

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
     * 身份证号
     */
    @ApiModelProperty("身份证号")
    private String idNumber;

    /**
     * 状态（参见字典：user_status）
     */
    @ApiModelProperty("状态（参见字典：user_status）")
    private Integer status;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long createUser;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人ID
     */
    @ApiModelProperty("修改人ID")
    private Long updateUser;

    /**
     * 修改人姓名
     */
    @ApiModelProperty("修改人姓名")
    private String updateUserName;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
