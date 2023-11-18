package com.yiling.user.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * ESB系统相关表配置
 *
 * @author: xuan.zhou
 * @date: 2022/11/24
 */
@Getter
@Setter
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "esb")
public class EsbConfig {

    /**
     * 管理员配置
     */
    private Admin admin;

    /**
     * 省区经理配置
     */
    private ProvinceManager provinceManager;

    /**
     * 省区经理配置
     */
    private CommonUser commonUser;

    /**
     * 需货计划人员配置
     */
    private DemandPlanUser demandPlanUser;

    @Getter
    @Setter
    @RefreshScope
    public static class Admin {

        /**
         * 管理员工号列表
         */
        private List<String> empIds;

        /**
         * 管理员绑定的角色编码列表
         */
        private List<String> roleCodes;
    }

    @Getter
    @Setter
    @RefreshScope
    public static class ProvinceManager {

        /**
         * 省区经理包含的职称列表
         */
        private List<String> jobNames;

        /**
         * 省区经理绑定的角色编码列表
         */
        private List<String> roleCodes;
    }

    @Getter
    @Setter
    @RefreshScope
    public static class CommonUser {

        /**
         * 普通用户绑定的角色编码列表
         */
        private List<String> roleCodes;
    }

    @Getter
    @Setter
    @RefreshScope
    public static class DemandPlanUser {

        /**
         * 需货计划人员全路径前缀
         */
        private List<String> fullPathPrefix;

        /**
         * 需货计划人员绑定的角色编码列表
         */
        private List<String> roleCodes;
    }
}
