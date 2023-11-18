package com.yiling.framework.common.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill;

/**
 * 逻辑删除
 * 1、根据ID逻辑删除
 * 2、批量逻辑删除
 *
 * @author: xuan.zhou
 * @date: 2020/7/11
 */
public class MySqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new LogicDeleteByIdWithFill());
        methodList.add(new LogicBatchDeleteWithFill());
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}
