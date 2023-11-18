package com.yiling.admin.system.system.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.util.Constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存用户信息 Form
 *
 * @author: lun.yu
 * @date: 2021/7/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class SaveAdminForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户名
     */
    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_USERNAME, message = "用户名格式错误")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 手机号
     */
    @NotEmpty
    @Pattern(regexp = Constants.REGEXP_MOBILE, message = "手机号格式错误")
    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 姓名
     */
    @NotEmpty
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    /**
     * 性别：1-男 2-女
     */
    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "性别", required = true)
    private Integer gender;

    /**
     * 出生年月日
     */
    @ApiModelProperty("出生日期")
    private Date birthday;

    /**
     * 邮箱
     */
    @Email
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 昵称
     */
    @JsonProperty("nickname")
    @ApiModelProperty("昵称")
    private String nickName;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

    /**
     * 角色ID集合
     */
    @ApiModelProperty("角色ID集合")
    private List<Long> roleIdList;
}
