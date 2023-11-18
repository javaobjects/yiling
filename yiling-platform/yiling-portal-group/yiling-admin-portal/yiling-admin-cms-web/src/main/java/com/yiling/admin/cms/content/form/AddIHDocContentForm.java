package com.yiling.admin.cms.content.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 添加IHDoc端内容
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddIHDocContentForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "内容List")
    private List<IHDocContentForm> contentList;

}