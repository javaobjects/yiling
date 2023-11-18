package com.yiling.dataflow.utils;

import org.apache.commons.lang3.StringUtils;

import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.enums.FlowTypeEnum;
import com.yiling.framework.common.util.SnowflakeIdUtils;

/**
 * @author fucheng.bai
 * @date 2023/6/29
 */
public class FlowDataIdUtils {

    public static String nextId(Integer flowClassify, Integer flowType) {
        // 获取id
        long id = SnowflakeIdUtils.snowflakeId();

        if (flowClassify != null && flowClassify != 0) {
            FlowClassifyEnum flowClassifyEnum = FlowClassifyEnum.getByCode(flowClassify);
            if (flowClassifyEnum != null && StringUtils.isNotEmpty(flowClassifyEnum.getPrefix())) {
                return flowClassifyEnum.getPrefix() + id;
            }
        }

        if (flowType != null && flowType != 0) {
            FlowTypeEnum flowTypeEnum = FlowTypeEnum.getByCode(flowType);
            if (flowTypeEnum != null && StringUtils.isNotEmpty(flowTypeEnum.getPrefix())) {
                return flowTypeEnum.getPrefix() + id;
            }
        }
        return String.valueOf(id);
    }
}
