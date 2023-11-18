package com.yiling.admin.cms.content.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 获取IHDoc文章详情
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-24
 */
@Data
@Accessors(chain = true)
public class QueryIHDocContentForm {

    @NotNull
    @ApiModelProperty(value = "id")
    private Long id;

}
