package com.yiling.open.erp.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/5/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ClientToolVersionDTO extends BaseDTO {

    /**
     * 版本号
     */
    private String version;

    /**
     * 版本名称
     */
    private String name;

    /**
     * 版本说明
     */
    private String description;

    /**
     * 安装包下载地址
     */
    private String packageUrl;

    /**
     * 文件DM5
     */
    private String  packageMd5;

    private String  guardUrl;
    /**
     * 创建时间
     */
    private Date createTime;
}
