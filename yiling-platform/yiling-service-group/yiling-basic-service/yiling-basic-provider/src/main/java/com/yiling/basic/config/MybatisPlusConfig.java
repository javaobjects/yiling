package com.yiling.basic.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yiling.framework.common.mybatis.config.BaseMybatisPlusConfig;

/**
 * Mybatis Plus Config
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
//@Import({ DataSourceConfiguration.class })
@Configuration
@EnableTransactionManagement
@MapperScan("com.yiling.basic.**.dao")
public class MybatisPlusConfig extends BaseMybatisPlusConfig {
}
