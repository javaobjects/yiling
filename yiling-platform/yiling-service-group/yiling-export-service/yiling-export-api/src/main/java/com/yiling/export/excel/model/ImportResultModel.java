package com.yiling.export.excel.model;

import java.util.List;

import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/21
 */
@Data
public class ImportResultModel<T> {
    /**
     * 失败条数
     */
    private Integer failCount;

    /**
     * 成功条数
     */
    private Integer successCount;

    /**
     * 结果集
     */
    private List<T> list;

    /**
     * 失败结果集
     */
    private List<T> failList;

    /**
     * 失败结果下载地址
     */
    @Deprecated
    private String failUrl;

    /**
     * 失败结果下载地址
     */
    private String resultUrl;
    /**
     * Sheet名称
     */
    private String sheetName;

    /**
     * 失败明细
     */
    @Deprecated
    private List<ImportResultModel.ImportResultDetailModel> importResultDetailList;

    /**
     * 失败明细实体
     */
    @Data
    @Deprecated
    public static class ImportResultDetailModel {

        /**
         * 行号
         */
        private Integer number;
        /**
         * 失败内容
         */
        private String content;
        /**
         * 失败信息
         */
        private String message;
        /**
         * sheet名称
         */
        private String sheetName ;
    }
}
