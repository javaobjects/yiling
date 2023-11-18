package com.yiling.framework.oss.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * oss上传文件的回调结果
 * 
 * @author xuan.zhou
 * @date 2022/6/8
 */
@Data
public class OssCallbackResult implements java.io.Serializable {

    private static final long serialVersionUID = 2255532763313963548L;

    @ApiModelProperty("存储空间")
    private String bucket;

    @ApiModelProperty("对象（文件）")
    private String object;

    @ApiModelProperty("文件的ETag")
    private String etag;

    @ApiModelProperty("Object大小，CompleteMultipartUpload时为整个Object的大小")
    private String size;

    @ApiModelProperty("文件的mimeType")
    private String mimeType;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("文件MD5")
    private String md5;

    @ApiModelProperty("文件URL")
    private String url;

    @ApiModelProperty("文件是否重复")
    private Boolean repeatFlag;

    @ApiModelProperty("图片信息")
    private ImageInfo imageInfo;

    @Data
    public static class ImageInfo {

        @ApiModelProperty("图片格式")
        private String format;

        @ApiModelProperty("图片宽度")
        private String width;

        @ApiModelProperty("图片高度")
        private String height;
    }

}
