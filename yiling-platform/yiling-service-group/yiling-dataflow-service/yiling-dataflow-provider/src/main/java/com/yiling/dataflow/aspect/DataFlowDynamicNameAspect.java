package com.yiling.dataflow.aspect;

import com.yiling.framework.common.aspect.DynamicNameAspect;
import com.yiling.framework.common.context.DynamicTableNameHelper;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 切面。用户解析spel表达式，并且赋值到 {@link DynamicTableNameHelper}中
 *
 * @author: pinlin
 */
@Aspect
@Component
public class DataFlowDynamicNameAspect extends DynamicNameAspect {
}
