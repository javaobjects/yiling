package com.yiling.admin.data.center.standard.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 SaveStandardGoodsTagsForm
 * @描述
 * @创建时间 2022/10/20
 * @修改人 shichen
 * @修改时间 2022/10/20
 **/
@Data
public class SaveStandardGoodsTagsForm {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "标准库ID", required = true)
    private Long standardId;

    @ApiModelProperty(value = "标准库标签ID列表", required = false)
    private List<Long> tagIds;
}
