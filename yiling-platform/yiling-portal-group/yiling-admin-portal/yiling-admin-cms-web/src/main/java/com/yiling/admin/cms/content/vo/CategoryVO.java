package com.yiling.admin.cms.content.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 栏目
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CategoryVO extends BaseVO {

    @ApiModelProperty(value = "业务线id")
    private Long lineId;

    @ApiModelProperty(value = "模块id")
    private Long moduleId;

    @ApiModelProperty(value = "栏目名称")
    private String categoryName;

    @ApiModelProperty(value = "0-禁用 1启用")
    private Integer status;

    @ApiModelProperty(value = "排序")
    private Integer categorySort;

    // @ApiModelProperty(value = "引用业务线")
    // List<Long> displayLines;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

}
