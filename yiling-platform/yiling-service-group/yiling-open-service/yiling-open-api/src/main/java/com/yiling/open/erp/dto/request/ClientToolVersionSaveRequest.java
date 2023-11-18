package com.yiling.open.erp.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ClientToolVersionSaveRequest extends BaseRequest {

    /**
     * ID
     */
    private Integer id;

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
     * 文件DM5
     */
    private String  packageMd5;
    /**
     * 安装包下载地址
     */
    private String packageUrl;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

}
