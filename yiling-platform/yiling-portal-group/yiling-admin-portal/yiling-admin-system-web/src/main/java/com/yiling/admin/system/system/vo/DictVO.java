package com.yiling.admin.system.system.vo;

import java.util.List;

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
public class DictVO implements java.io.Serializable {

    /**
     * 字典名称
     */
    @ApiModelProperty("字典名称")
    private String name;

    /**
     * 字典描述
     */
    @ApiModelProperty("字典描述")
    private String description;

    /**
     * 字典内容
     */
    @ApiModelProperty("字典内容")
    private List<DictData> dataList;

    @Data
    @ApiModel
    public static class DictData implements java.io.Serializable {

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
         * 是否默认：0-否 1-是
         */
        @ApiModelProperty("是否默认：0-否 1-是")
        private Integer defaultFlag;
    }
}
