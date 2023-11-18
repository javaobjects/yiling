package com.yiling.export;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.slf4j.Slf4j;

/**
 * Base Test
 *
 * @author: xuan.zhou
 * @date: 2020/6/16
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ExportProviderApplication.class)
public class BaseTest {
}