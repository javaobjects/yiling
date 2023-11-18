package com.yiling.framework.common.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件信息 VO
 *
 * @author: xuan.zhou
 * @date: 2021/7/19
 */
@Data
public class FileInfoVO {

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
}
