package com.yiling.admin.system.system.form;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 系统登录日志 Form
 *
 * @author: lun.yu
 * @date: 2021/12/31
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySysLoginLogPageListForm extends QueryPageListForm {

    /**
     * 应用ID（参考AppEnum）
     */
    @ApiModelProperty("应用ID")
    private String appId;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 登录账号
     */
    @ApiModelProperty("登录账号")
    @Length(max = 20)
    private String loginAccount;

    /**
     * 开始登录时间
     */
    @ApiModelProperty("开始登录时间（精确到秒）")
    private Date startLoginTime;

    /**
     * 结束登录时间
     */
    @ApiModelProperty("结束登录时间（精确到秒）")
    private Date endLoginTime;

}
