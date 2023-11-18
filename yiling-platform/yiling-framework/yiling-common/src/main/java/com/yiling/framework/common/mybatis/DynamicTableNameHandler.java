package com.yiling.framework.common.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.yiling.framework.common.context.DynamicTableNameHelper;
import org.springframework.util.StringUtils;

public class DynamicTableNameHandler implements TableNameHandler {
    @Override
    public String dynamicTableName(String sql, String tableName) {
        String tableNameSuffix = DynamicTableNameHelper.get();
        if (StringUtils.hasText(tableNameSuffix)) {
            return tableName + "_" + tableNameSuffix;
        }
        return tableName;
    }
}
