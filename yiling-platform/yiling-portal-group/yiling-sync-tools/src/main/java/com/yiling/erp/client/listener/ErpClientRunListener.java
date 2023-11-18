package com.yiling.erp.client.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author shuan
 */
public class ErpClientRunListener implements ServletContextListener {

    //启动服务初始化方法
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //加载配置信息
    }

    //销毁服务器方法
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
