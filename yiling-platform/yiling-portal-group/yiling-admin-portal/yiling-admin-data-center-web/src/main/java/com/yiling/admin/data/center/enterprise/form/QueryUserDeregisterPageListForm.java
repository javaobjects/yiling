package com.yiling.admin.data.center.enterprise.form;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注销账号分页查询 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryUserDeregisterPageListForm extends QueryPageListForm {

    @NotNull
    @Range(min = 1, max = 3)
    @ApiModelProperty(value = "注销状态：1-待注销 2-已注销 3-已撤销", required = true)
    private Integer status;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 开始申请注销时间
     */
    @ApiModelProperty("开始申请注销时间")
    private Date startApplyTime;

    /**
     * 结束申请注销时间
     */
    @ApiModelProperty("结束申请注销时间")
    private Date endApplyTime;

}
