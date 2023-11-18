package com.yiling.goods.config;

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
@Configuration
@EnableTransactionManagement
@MapperScan("com.yiling.goods.**.dao")
public class MybatisPlusConfig extends BaseMybatisPlusConfig {
}
