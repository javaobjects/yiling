package com.yiling.open.cms.diagnosis.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据字典 VO
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@Data
@ApiModel
public class DictDataVO implements java.io.Serializable {

    /**
     *字典类型表id
     */
    @ApiModelProperty("字典内容Id")
    private Long id;

    /**
     * 字典类型ID（关联dict_type.id字段）
     */
    @ApiModelProperty("字典类型Id")
    private Long typeId;

    /**
     * 字典标签
     */
    @ApiModelProperty("字典标签")
    private String label;

    /**
     * 字典键值
     */
    @ApiModelProperty("字典键值")
    private String value;

    /**
     * 字典描述
     */
    @ApiModelProperty("字典描述")
    private String description;


    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    /**
     * 字典排序
     */
    @ApiModelProperty("字典排序")
    private Integer sort;


}
