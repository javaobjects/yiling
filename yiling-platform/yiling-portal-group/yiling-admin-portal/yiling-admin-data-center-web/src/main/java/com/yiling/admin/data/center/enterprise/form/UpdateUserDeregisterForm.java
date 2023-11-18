package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审核用户注销账号 Form
 *
 * @author: lun.yu
 * @date: 2022-06-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateUserDeregisterForm extends BaseForm {

    /**
     * ID
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 注销状态：1-待注销 2-已注销 3-已撤销
     */
    @NotNull
    @Range(min = 2, max = 3)
    @ApiModelProperty("注销状态：1-待注销 2-已注销 3-已撤销")
    private Integer status;

}
