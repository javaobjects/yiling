package com.yiling.dataflow.config;

import java.util.HashMap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.yiling.framework.common.mybatis.DynamicTableNameHandler;
import com.yiling.framework.common.mybatis.config.BaseMybatisPlusConfig;

/**
 * Mybatis Plus Config
 *
 * @author: xuan.zhou
 * @date: 2021/5/13
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.yiling.dataflow.**.dao")
public class MybatisPlusConfig  extends BaseMybatisPlusConfig{
    //Mybatis-plus实现动态表名查询复写mybatisPlusInterceptor 增加
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        // 需要放到第一位，切记不要放错，不然会导致动态表名切换失效
        HashMap<String, TableNameHandler> map = new HashMap<String, TableNameHandler>(14);
        map.put("crm_enterprise",dynamicTableNameHandler());
        map.put("crm_supplier",dynamicTableNameHandler());
        map.put("crm_pharmacy",dynamicTableNameHandler());
        map.put("crm_hospital",dynamicTableNameHandler());
        map.put("crm_department_product_relation",dynamicTableNameHandler());
        map.put("crm_department_area_relation",dynamicTableNameHandler());
        map.put("crm_enterprise_relation_ship",dynamicTableNameHandler());
        map.put("crm_goods_info",dynamicTableNameHandler());
        map.put("crm_goods_group",dynamicTableNameHandler());
        map.put("crm_goods_group_relation",dynamicTableNameHandler());
        map.put("crm_goods_category",dynamicTableNameHandler());
        map.put("crm_goods_tag",dynamicTableNameHandler());
        map.put("crm_goods_tag_relation",dynamicTableNameHandler());
        map.put("crm_enterprise_relation_pinch_runner",dynamicTableNameHandler());
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
