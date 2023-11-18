package com.yiling.user.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.yiling.framework.common.mybatis.DynamicTableNameHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yiling.framework.common.mybatis.config.BaseMybatisPlusConfig;

import java.util.HashMap;

/**
 * Mybatis Plus Config
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.yiling.user.**.dao")
public class MybatisPlusConfig extends BaseMybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        // 需要放到第一位，切记不要放错，不然会导致动态表名切换失效
        HashMap<String, TableNameHandler> map = new HashMap<String, TableNameHandler>(2);
        map.put("esb_employee",dynamicTableNameHandler());
        map.put("esb_organization",dynamicTableNameHandler());
        map.put("esb_job",dynamicTableNameHandler());
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
        PaginationInnerInterceptor paginationInnerInterceptor= new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(2000L);
        // 需要放到第一位，切记不要放错，不然会导致动态表名切换失效
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }
    @Bean
    public DynamicTableNameHandler dynamicTableNameHandler() {
        return new DynamicTableNameHandler();
    }
}
