package com.yiling.erp.client.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import cn.hutool.core.util.ObjectUtil;

public class CommonConstants {
    public static String getString(Object object)
            throws ErpException
    {
        try
        {
            if (ObjectUtil.isNull(object)) {
                return null;
            }
            return object.toString();
        } catch (Exception e) {
            throw new ErpException("该字段类型[" + object.getClass() + "] , 数值[" + object.toString() + "]转换失败！", e);
        }
    }

    public static Integer getInteger(Object object) throws ErpException {
        try {
            if (ObjectUtil.isNull(object)) {
                return null;
            }
            return Integer.valueOf(Integer.parseInt(object.toString()));
        } catch (Exception e) {
            throw new ErpException("该字段类型[" + object.getClass() + "] , 数值[" + object.toString() + "]转换失败！", e);
        }
    }

    public static Long getLong(Object object) throws ErpException {
        try {
            if (ObjectUtil.isNull(object)) {
                return null;
            }
            return Long.valueOf(Long.parseLong(object.toString()));
        } catch (Exception e) {
            throw new ErpException("该字段类型[" + object.getClass() + "] , 数值[" + object.toString() + "]转换失败！", e);
        }
    }

    public static String getDate(Object object, String format) throws ErpException {
        try {
            if (ObjectUtil.isNull(object)) {
                return null;
            }
            if ((object instanceof Date)) {
                return Utils.dateFormateToString((Date)object, format);
            }
            if ((object instanceof String)) {
                return object.toString();
            }
            if ((object instanceof Integer)) {
                return object.toString();
            }
            throw new ErpException("该字段类型[" + object.getClass() + "]不存在");
        }
        catch (Exception e) {
            throw new ErpException("该字段类型[" + object.getClass() + "] , 数值[" + object.toString() + "]转换失败！", e);
        }
    }

    public static synchronized BigDecimal getBigDecimal(Object object) throws ErpException {
        try {
            if (ObjectUtil.isNull(object)) {
                return null;
            }

            if ((object instanceof Integer))
            {
                return new BigDecimal(((Integer)object).intValue());
            }
            if ((object instanceof Long))
            {
                return new BigDecimal(((Long)object).longValue());
            }
            if ((object instanceof Double))
            {
                return new BigDecimal(((Double)object).doubleValue());
            }
            if ((object instanceof BigDecimal)) {
                return (BigDecimal)object;
            }
            if ((object instanceof String)) {
                return new BigDecimal((String)object);
            }
            if ((object instanceof Number)) {
                return new BigDecimal(((Number)object).doubleValue());
            }
            if ((object instanceof BigInteger)) {
                return new BigDecimal((BigInteger)object);
            }
            if ((object instanceof Float)) {
                return new BigDecimal(object.toString());
            }
            if ((object instanceof Byte)) {
                return new BigDecimal(object.toString());
            }
            if ((object instanceof Short)) {
                return new BigDecimal(object.toString());
            }
            if ((object instanceof Boolean)) {
                if (((Boolean)object).booleanValue()) {
                    return new BigDecimal(1);
                }
                return new BigDecimal(0);
            }

            throw new ErpException("该字段类型[" + object.getClass() + "]不存在");
        }
        catch (ErpException e) {
            throw e;
        } catch (Exception e) {
            throw new ErpException("该字段类型[" + object.getClass() + "] , 数值[" + object.toString() + "]转换失败！", e);
        }
    }

    public static String getDateForQuery(Object object, String format) throws ErpException {
        try {
            if (ObjectUtil.isNull(object)) {
                return null;
            }
            if ((object instanceof Date)) {
                return Utils.dateFormateToString((Date)object, format);
            } else {
                return object.toString();
            }
        }
        catch (Exception e) {
            throw new ErpException("该字段类型[" + object.getClass() + "] , 数值[" + object.toString() + "]转换失败！", e);
        }
    }
}
