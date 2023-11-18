package com.yiling.erp.client.log;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.yiling.erp.client.service.impl.ClientLogServiceImpl;
import com.yiling.erp.client.util.SpringUtils;

/**
 * @author: shuang.zhang
 * @date: 2021/12/9
 */
@Plugin(name = "ThriftAppender", category = "Core", elementType = "appender", printObject = true)
public class ThriftAppender extends AbstractAppender {

    /**
     * 构造函数
     */
    public ThriftAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    /**
     * 接收配置文件中的参数
     */
    @PluginFactory
    public static ThriftAppender createAppender(@PluginAttribute("name") String name,
                                                @PluginElement("Filter") final Filter filter,
                                                @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
        if (name == null) {
            LOGGER.error("no name defined in conf.");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new ThriftAppender(name, filter, layout, ignoreExceptions);
    }

    @Override
    public void append(LogEvent event) {
        final byte[] bytes = getLayout().toByteArray(event);
        try {
            ClientLogServiceImpl clientLogService = SpringUtils.getBean("clientLogService");
            clientLogService.sendLog(new String(bytes));
        } catch (Exception e) {
            LOGGER.error("调用日志接口报错", e);
        }
    }
}
