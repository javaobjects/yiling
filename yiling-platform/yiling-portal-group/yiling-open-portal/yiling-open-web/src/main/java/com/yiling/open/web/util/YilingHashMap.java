package com.yiling.open.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.yiling.open.erp.util.OpenConstants;
import com.yiling.open.erp.util.SignatureAlgorithm;

/**
 * 纯字符串字典结构
 *                       
 * @Filename: YilingHashMap.java
 * @Version: 1.0
 * @Author: fenghu
 * @Email: fenghu.liu@rograndec.com
 *
 */
public class YilingHashMap extends HashMap<String, String> {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2686585148515803434L;

    public YilingHashMap() {
        super();
    }

    public YilingHashMap(Map<? extends String, ? extends String> m) {
        super(m);
    }

    public String put(String key, Object value) {
        String strValue;

        if (value == null) {
            strValue = null;
        } else if (value instanceof String) {
            strValue = (String) value;
        } else if (value instanceof Integer) {
            strValue = ((Integer) value).toString();
        } else if (value instanceof Long) {
            strValue = ((Long) value).toString();
        } else if (value instanceof Float) {
            strValue = ((Float) value).toString();
        } else if (value instanceof Double) {
            strValue = ((Double) value).toString();
        } else if (value instanceof Boolean) {
            strValue = ((Boolean) value).toString();
        } else if (value instanceof Date) {
            DateFormat format = new SimpleDateFormat(OpenConstants.DATE_TIME_FORMAT);
            format.setTimeZone(TimeZone.getTimeZone(OpenConstants.DATE_TIMEZONE));
            strValue = format.format((Date) value);
        } else {
            strValue = value.toString();
        }

        return this.put(key, strValue);
    }

    @Override
    public String put(String key, String value) {
        if (SignatureAlgorithm.areNotEmpty(key, value)) {
            return super.put(key, value);
        } else {
            return null;
        }
    }

}
