package com.yiling.bi.resource.handler;

import java.math.BigDecimal;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import org.apache.commons.lang.ArrayUtils;

/**
 * @author: shuang.zhang
 * @date: 2022/9/27
 */
public class ExcelHandler extends ExcelDataHandlerDefaultImpl {

    @Override
    public Object importHandler(Object obj, String name, Object value) {
//        if (ArrayUtils.contains(this.getNeedHandlerFields(), name)) {
//            if (value == null) {
//                return BigDecimal.ZERO;
//            }
//            return new BigDecimal(String.valueOf(value));
//        } else {
            return super.importHandler(obj, name, value);
//        }

    }

}
