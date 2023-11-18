package com.yiling.export.excel.handler;

import java.util.List;
import java.util.Map;

/**
 * @author: shuang.zhang
 * @date: 2021/5/28
 */
public interface ImportDataHandler<T> {

    /**
     * 是否开启事务,这里需要第三方事务
     * @return
     */
    default Boolean isOpenTransaction() {
        return false;
    }

    /**
     * 处理导入单条数据入库
     * @param object
     * @param paramMap
     * @return 返回失败或者成功
     */
    List<T> execute(List<T> object, Map<String,Object> paramMap);
}
