package com.yiling.sjms.demo;

import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import com.yiling.sjms.BaseTest;
import com.yiling.sjms.demo.api.DemoApi;

import lombok.extern.slf4j.Slf4j;

/**
 * DEMO TEST
 *
 * @author xuan.zhou
 * @date 2022/5/12
 */
@Slf4j
public class DemoTest extends BaseTest {

    @DubboReference
    DemoApi demoApi;

    @Test
    public void echo() {
        String str = demoApi.echo("Leo");
        log.info("str:{}", str);
    }

}
