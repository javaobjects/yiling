package com.yiling.framework.common.base;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.EscapeUtil;

/**
 * BaseController
 *
 * @author xuan.zhou
 * @date 2020/06/16
 */
public class BaseController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : EscapeUtil.escapeHtml4(text.trim()));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });

        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if(!StringUtils.isEmpty(text) && !"null".equals(text)){
                    setValue(DateUtil.parse(text, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
                }else {
                    setValue(null);
                }
            }
        });

        // Timestamp 类型转换
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Date date = DateUtil.parse(text, "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
                setValue(date == null ? null : new Timestamp(date.getTime()));
            }
        });
    }
}
