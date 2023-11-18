package com.yiling.basic.dict.bo;

import java.util.List;

import lombok.Data;

/**
 * 数据字典 BO
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@Data
public class DictBO implements java.io.Serializable {

    /**
     * 字典ID
     */
    private Long id;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典描述
     */
    private String description;

    /**
     * 字典内容
     */
    private List<DictData> dataList;

    @Data
    public static class DictData implements java.io.Serializable {

        /**
         * 字典标签
         */
        private String label;

        /**
         * 字典键值
         */
        private String value;

        /**
         * 字典描述
         */
        private String description;

        /**
         * 是否默认：0-否 1-是
         */
        private Integer defaultFlag;
    }
}
