package com.yiling.framework.common.json.jackson;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 自定义Jackson反序列化日期类型时应用的类型转换器,一般用于@RequestBody接受参数时使用
 * 
 * @author xuan.zhou
 * @date 2020/8/7
 */
public class DateJacksonConverter extends JsonDeserializer<Date> {

    private static String[] parsePatterns = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH:mm:ss" };

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String dateStr = p.getText();
        if (StrUtil.isNotEmpty(dateStr)) {
            return DateUtil.parse(dateStr, parsePatterns);
        }
        return null;
    }

    @Override
    public Class<?> handledType() {
        return Date.class;
    }
}