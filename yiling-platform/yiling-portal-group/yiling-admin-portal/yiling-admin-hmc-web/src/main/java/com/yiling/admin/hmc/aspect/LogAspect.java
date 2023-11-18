package com.yiling.admin.hmc.aspect;

import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.yiling.basic.log.api.SysOperLogApi;
import com.yiling.basic.log.dto.SysOperLogDTO;
import com.yiling.framework.common.log.AbstractLogAspect;
import com.yiling.framework.common.log.SysOperLog;
import com.yiling.framework.common.log.enums.SystemEnum;
import com.yiling.framework.common.util.PojoUtils;

/**
 * 日志切面
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
@Aspect
@Component
public class LogAspect extends AbstractLogAspect {

    @DubboReference(async = true)
    SysOperLogApi sysOperLogApi;

    @Override
    protected void process(SysOperLog operLog) {
        sysOperLogApi.save(PojoUtils.map(operLog, SysOperLogDTO.class));
    }

    @Override
    protected void setSystemId(SysOperLog operLog) {
        operLog.setSystemId(SystemEnum.ADMIN_HMC.getCode());
    }
}
