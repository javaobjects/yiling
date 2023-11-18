package com.yiling.admin.cms.content.config;

import lombok.Data;

/**
 * 模块配置信息
 */
@Data
public class ModulesConfig {

    /**
     * id
     */
    private Long moduleId;

    /**
     * 模块名称
     */
    private String moduleName;
}
