package com.yiling.basic.location.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.yiling.basic.BaseTest;
import com.yiling.basic.location.bo.IPLocationBO;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2022/10/18
 */
@Slf4j
public class IPLocationServiceTest extends BaseTest {

    @Autowired
    IPLocationService ipLocationService;

    @Test
    public void query() {
        IPLocationBO ipLocationBO = ipLocationService.query("183.94.141.180");
        log.info(ipLocationBO.toString());
    }

    @Test
    public void check() {
        ipLocationService.check();
    }
}
