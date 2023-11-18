package com.yiling.framework.common.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.yiling.framework.common.mybatis.DynamicTableNameHandler;
import org.springframework.context.annotation.Bean;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.mybatis.MySqlInjector;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * BaseMybatisPlusConfig
 *
 * @author xuan.zhou
 * @date 2019/10/17
 */
public class BaseMybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor= new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(2000L);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new MyMetaHandler());
        return globalConfig;
    }

    @Bean
    public MySqlInjector mySqlInjector() {
        MySqlInjector mySqlInjector = new MySqlInjector();
        return mySqlInjector;
    }

    @Bean
    public MyMetaHandler myMetaHandler() {
        MyMetaHandler myMetaHandler = new MyMetaHandler();
        return myMetaHandler;
    }
}
