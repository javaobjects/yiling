package com.yiling.export.export.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ExportDataDTO implements Serializable {

    private static final long serialVersionUID = 3386547486216174327L;

    /**
     * 工作表标题
     */
    private String sheetName;

    /**
     * 需要特殊转换列的英文名和对应的转换规则，不需要设为null
     */
    private Map<String, Map<Object, String>> enField;

    /**
     * 数据
     */
    private List<Map<String, Object>>     data;

    /**
     * 第一行名称
     */
    private LinkedHashMap<String, String> fieldMap;

    private LinkedList<String> templeteParamList;
    // 数据起始行
    private int fristRow;

}