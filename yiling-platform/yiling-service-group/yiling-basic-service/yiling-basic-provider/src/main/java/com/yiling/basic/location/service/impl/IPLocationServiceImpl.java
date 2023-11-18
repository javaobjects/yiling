package com.yiling.basic.location.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.yiling.basic.location.bo.IPLocationBO;
import com.yiling.basic.location.factory.IPLocationQueryServiceFactory;
import com.yiling.basic.location.service.IPLocationService;
import com.yiling.basic.sms.enums.SmsTypeEnum;
import com.yiling.basic.sms.service.SmsService;

import lombok.extern.slf4j.Slf4j;

/**
 * IP归属地查询 Service 实现
 *
 * @author: xuan.zhou
 * @date: 2022/10/18
 */
@Slf4j
@Service("ipLocationQueryService")
@RefreshScope
public class IPLocationServiceImpl implements IPLocationService {

    @Value("${ip.location.verify.sms.receivers}")
    private String smsReceivers;

    @Resource
    IPLocationQueryServiceFactory factory;
    @Autowired
    SmsService smsService;

    @Override
    public IPLocationBO query(String ip) {
        Assert.notNull(ip, "IP地址不能为空");
        return factory.getService().query(ip);
    }

    @Override
    public void check() {
        boolean result = factory.getService().verify();
        if (!result) {
            log.error("获取IP归属地服务已失效，请尽快确认。实现类:{}", factory.getService().getClass().getName());
            for (String receiver : smsReceivers.split(",")) {
                smsService.send(receiver, "获取IP归属地服务已失效，请尽快确认", SmsTypeEnum.OPERATIONAL_NOTICE);
            }
        } else {
            log.info("检查完毕：获取IP归属地服务正常。");
        }
    }
}
