package com.yiling.basic.no.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.BaseTest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2023/2/28
 */
@Slf4j
public class NoServiceTest extends BaseTest {

    @Autowired
    NoService noService;

    @Test
    public void gen() {
        for (int i = 0; i < 100; i++) {
            String no = noService.gen(TestEnum.NO_TEST);
            log.info("no = {}", no);
        }
    }
}


