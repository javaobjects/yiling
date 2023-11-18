package com.yiling.user.aspect;

import com.yiling.framework.common.aspect.DynamicNameAspect;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserDynamicNameAspect extends DynamicNameAspect {
}
