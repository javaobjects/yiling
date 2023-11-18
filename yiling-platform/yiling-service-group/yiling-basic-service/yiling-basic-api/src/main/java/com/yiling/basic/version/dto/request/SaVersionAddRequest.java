package com.yiling.basic.version.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaVersionAddRequest extends BaseRequest {

    private String name;

    private Integer forceUpgradeFlag;

    private String version;

    private String description;

    private Integer appType;

    private String packageUrl;

    /**
     * 文件大小
     */
    private long packageSize;

    /**
     * 文件MD5值
     */
    private String packageMd5;
}
