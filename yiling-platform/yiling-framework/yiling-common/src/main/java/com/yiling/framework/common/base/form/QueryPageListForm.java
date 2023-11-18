package com.yiling.framework.common.base.form;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页列表查询表单
 *
 * @author: xuan.zhou
 * @date: 2019/12/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class QueryPageListForm extends BaseForm {

    @ApiModelProperty(value = "第几页，默认：1", example = "1")
    private int current = 1;

    @ApiModelProperty(value = "每页记录数，默认：10", example = "10")
    private int size = 10;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public Page getPage() {
        return new Page(this.current, this.size);
    }
}
