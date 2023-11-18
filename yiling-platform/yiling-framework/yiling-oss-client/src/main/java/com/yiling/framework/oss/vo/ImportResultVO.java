package com.yiling.framework.oss.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
@Data
public class ImportResultVO {

    @ApiModelProperty(value = "失败条数")
    private Integer failCount;

    @ApiModelProperty(value = "成功条数")
    private Integer successCount;

    @ApiModelProperty(value = "失败文件导出url地址")
    private String failUrl;

    @ApiModelProperty(value = "标题名称")
    private String sheetName;

    @ApiModelProperty(value = "失败明细")
    private List<ImportResultDetailVO> importResultDetailList;

    @Data
    public static class ImportResultDetailVO {
        @ApiModelProperty(value = "失败行数")
        private Integer number;
        @ApiModelProperty(value = "字段内容")
        private String  content;
        @ApiModelProperty(value = "失败信息")
        private String  message;
    }
}

