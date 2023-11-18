package com.yiling.sales.assistant.app.mr.question.vo;

import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileReplyVO extends FileInfoVO {
    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;
}
