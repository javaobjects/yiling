package com.yiling.sales.assistant.app.homepage.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.sales.assistant.app.system.form.LocationTreeForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 完善小三元个人信息 Form
 *
 * @author: xuan.zhou
 * @date: 2022/1/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CompleteXiaoSanYuanInfoForm extends BaseForm {

    @NotEmpty
    @Length(max = 20)
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @NotEmpty
    @Length(max = 18)
    @ApiModelProperty(value = "身份证号", required = true)
    private String idNumber;

    @NotEmpty
    @ApiModelProperty(value = "销售区域")
    private List<LocationTreeForm> salesAreaTree;

}
