package com.yiling.admin.b2b.integral.form;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分发放规则基本信息 Form
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralGiveRuleBasicForm extends BaseForm {

    /**
     * ID
     */
    @ApiModelProperty("ID")
    private Long id;

    /**
     * 规则名称
     */
    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "规则名称", required = true)
    private String name;

    /**
     * 规则生效时间
     */
    @NotNull
    @ApiModelProperty(value = "规则生效时间", required = true)
    private Date startTime;

    /**
     * 规则失效时间
     */
    @NotNull
    @ApiModelProperty(value = "规则失效时间", required = true)
    private Date endTime;

    /**
     * 规则说明
     */
    @NotEmpty
    @Length(max = 50)
    @ApiModelProperty("规则说明")
    private String description;

    /**
     * 行为ID
     */
    @NotNull
    @ApiModelProperty(value = "行为ID", required = true)
    private Long behaviorId;

}
