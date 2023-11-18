package com.yiling.activity.web.wx.factory;

import com.yiling.activity.web.wx.enums.WxTypeEnum;
import com.yiling.activity.web.wx.service.WxMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信消息工厂类
 *
 * @Author fan.shen
 * @Date 2022/7/22
 **/
@Slf4j
@Component
public class WxMsgFactory implements ApplicationContextAware {

    /**
     * 处理微信消息服务容器
     */
    private static final Map<WxTypeEnum, WxMsgService> container = new ConcurrentHashMap<>();

    /**
     * 根据appId获取对应消息服务类，处理消息
     *
     * @param appId
     * @return
     */
    public WxMsgService getWxMsgServiceByAppId(String appId) {
        return container.get(WxTypeEnum.getByAppId(appId));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        // 获取所有WxMsgService实现类
        Map<String, WxMsgService> wxMsgServices = applicationContext.getBeansOfType(WxMsgService.class);

        // 放入容器
        wxMsgServices.values().forEach(item -> container.put(item.getWxType(), item));

        log.info("微信消息服务容器初始化完成...");
    }

}
