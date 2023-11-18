package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.user.system.api.UserDeregisterAccountApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 注销账号定时任务
 *
 * @author: lun.yu
 * @date: 2022-07-05
 */
@Component
@Slf4j
public class UserDeregisterAccountJobHandler {

    @DubboReference
    UserDeregisterAccountApi userDeregisterAccountApi;

    /**
     * 注销账号定时任务：每30分钟进行检查是否存在达到24小时需要执行注销的账号
     *
     * @param param
     * @return
     * @throws Exception
     */
    @JobLog
    @XxlJob("userDeregisterAccountJobHandler")
    public ReturnT<String> userDeregisterAccountJobHandler(String param) throws Exception {
        log.info("任务开始：注销账号定时任务");
        userDeregisterAccountApi.deregisterAccountTask();
        log.info("任务结束：注销账号定时任务");
        return ReturnT.SUCCESS;
    }
}
