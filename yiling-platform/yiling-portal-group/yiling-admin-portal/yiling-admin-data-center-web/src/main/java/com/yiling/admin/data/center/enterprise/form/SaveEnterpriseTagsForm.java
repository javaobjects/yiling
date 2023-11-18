package com.yiling.admin.data.center.enterprise.form;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存企业标签信息 Form
 *
 * @author: xuan.zhou
 * @date: 2021/10/14
 */
@Data
public class SaveEnterpriseTagsForm {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "企业ID", required = true)
    private Long eid;

    @ApiModelProperty(value = "企业标签ID列表", required = false)
    private List<Long> tagIds;
}
