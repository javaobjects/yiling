package com.yiling.user.system.dto.request;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建神机妙算系统用户 Request
 *
 * @author: xuan.zhou
 * @date: 2022/11/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateSjmsUserRequest extends BaseRequest {

    private static final long serialVersionUID = 1787354762240438166L;

    /**
     * ESB人员工号
     */
    private String empId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 出生年月日
     */
    private Date birthday;

    /**
     * 手机号
     */
    @NotEmpty
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 当用户信息存在时，选择忽略
     */
    private boolean ignoreExists = false;
}
