package com.yiling.sjms.gb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/7/19
 */
@Data
public class FileNameInfoVO {

    /**
     * 文件KEY
     */
    @ApiModelProperty("文件KEY")
    private String fileKey;

    /**
     * 文件地址
     */
    @ApiModelProperty("文件地址")
    private String fileUrl;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件Md5")
    private String fileMd5;
}
