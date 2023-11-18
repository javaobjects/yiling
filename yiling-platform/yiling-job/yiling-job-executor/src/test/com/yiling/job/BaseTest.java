package com.yiling.job;

import com.yiling.job.executor.JobExecutorApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base Test
 *
 * @author: xuan.zhou
 * @date: 2020/6/16
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JobExecutorApplication.class)
public class BaseTest {
}
