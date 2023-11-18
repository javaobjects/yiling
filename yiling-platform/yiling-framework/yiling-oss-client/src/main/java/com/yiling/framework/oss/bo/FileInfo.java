package com.yiling.framework.oss.bo;

import java.util.Date;

import lombok.Data;

/**
 * 文件信息
 *
 * @author: xuan.zhou
 * @date: 2021/5/14
 */
@Data
public class FileInfo implements java.io.Serializable {

    private static final long serialVersionUID = 8183787119293512439L;

    /**
     * 原始文件名
     */
    private String            name;

    /**
     * 文件存储KEY
     */
    private String            key;

    /**
     * 是否图片
     */
    private Boolean           isImg;

    /**
     * 上传文件类型
     */
    private String            contentType;

    /**
     * 文件大小
     */
    private long              size;

    /**
     * 文件MD5值
     */
    private String            md5;

    /**
     * oss访问路径
     */
    private String            url;

    /**
     * 上传时间
     */
    private Date              createTime;
}
