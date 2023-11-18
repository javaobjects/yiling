package com.yiling.export.excel.service;

import java.io.InputStream;
import java.util.Map;

import com.yiling.export.excel.model.ImportResultModel;

/**
 * @author: shuang.zhang
 * @date: 2022/11/17
 */
public interface BaseExcelImportService {

    /**
     * 导入excel接口
     * @param inputstream 文件流
     * @param readRows 读取的行数（自行设置）
     * @param readRows 传递的参数
     * @return
     */
    ImportResultModel importExcel(InputStream inputstream, Integer readRows, Map<String,Object> param) throws Exception;
}
