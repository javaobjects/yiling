package com.yiling.open.cms.content.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/12/05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class IHPatientContentCategoryVO extends BaseVO {

    /**
     * 模块id
     */
    @ApiModelProperty("模块id")
    private Long moduleId;

    /**
     * 栏目id
     */
    @ApiModelProperty(value = "栏目id")
    private Long categoryId;

    /**
     * 栏目名称
     */
    @ApiModelProperty("栏目名称")
    private String categoryName;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer categoryRank;


    /**
     * 栏目点击量
     */
    @ApiModelProperty("栏目点击量")
    private Integer view;
}
