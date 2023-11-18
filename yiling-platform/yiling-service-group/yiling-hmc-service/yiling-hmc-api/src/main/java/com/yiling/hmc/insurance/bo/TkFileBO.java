package com.yiling.hmc.insurance.bo;

import java.io.Serializable;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/7/5
 */
@Data
public class TkFileBO implements Serializable {

    /**
     * qingstor文件地址(带后缀)
     */
    private String filePath;

    /**
     * 文件类型
     */
    private String fileTypeCode;

    /**
     * 资料类型 00-图片,01-视频,02-PDF,03-WORD等
     */
    private String mediaType;
}
