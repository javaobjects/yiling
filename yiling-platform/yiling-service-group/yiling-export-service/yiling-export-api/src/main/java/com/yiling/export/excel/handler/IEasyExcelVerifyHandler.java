package com.yiling.export.excel.handler;

import com.alibaba.excel.context.AnalysisContext;

/**
 * @author: shuang.zhang
 * @date: 2023/5/9
 */
public interface IEasyExcelVerifyHandler<T> {
    boolean verifyHandler(T var1, AnalysisContext context);
}
