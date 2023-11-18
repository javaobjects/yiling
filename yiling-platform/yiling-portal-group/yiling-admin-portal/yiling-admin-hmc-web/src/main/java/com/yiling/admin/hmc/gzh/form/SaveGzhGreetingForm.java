package com.yiling.admin.hmc.gzh.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 保存公众号欢迎语 Form
 *
 * @author: fan.shen
 * @date: 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGzhGreetingForm extends BaseForm {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("场景id")
    private Integer sceneId;

    @ApiModelProperty(value = "草稿版本")
    private String draftVersion;

    @ApiModelProperty("运营备注")
    private String remark;
}
