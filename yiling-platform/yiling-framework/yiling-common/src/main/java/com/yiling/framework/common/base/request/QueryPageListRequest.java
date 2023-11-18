package com.yiling.framework.common.base.request;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 分页查询 Request
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryPageListRequest extends BaseRequest {

    private static final long serialVersionUID = 1515883924920299249L;

    @ApiModelProperty(value = "每页显示条数，默认10", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页，默认1", example = "1")
    private Integer current = 1;

    public <T> Page<T> getPage() {
        return new Page<>(this.current, this.size);
    }
}
