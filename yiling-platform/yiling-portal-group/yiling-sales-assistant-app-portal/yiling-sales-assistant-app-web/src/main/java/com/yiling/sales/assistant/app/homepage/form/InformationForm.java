package com.yiling.sales.assistant.app.homepage.form;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手-信息反馈From
 * @author lun.yu
 * @version V1.0
 * @date: 2021/9/27
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InformationForm extends BaseForm {

    /**
     * 信息描述
     */
    @NotEmpty
    @Length(max = 200)
    @ApiModelProperty("信息描述")
    private String content;


}
