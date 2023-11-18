package com.yiling.basic.version.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

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
public class SaVersionDTO extends BaseDTO {
    private String name;

    private String version;

    private String description;

    private Integer appType;

    private String packageUrl;

    private Long createUser;

    private Date createTime;
}
