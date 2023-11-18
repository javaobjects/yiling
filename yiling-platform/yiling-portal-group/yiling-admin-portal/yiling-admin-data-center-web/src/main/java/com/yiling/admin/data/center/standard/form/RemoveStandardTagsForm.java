package com.yiling.admin.data.center.standard.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 RemoveStandardTagsForm
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class RemoveStandardTagsForm extends BaseForm {
    @NotEmpty
    @ApiModelProperty(value = "标签ID集合")
    private List<Long> tagsIdList;
}
