package com.yiling.open.third.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.open.third.entity.FlowInterfaceConfigDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-04-11
 */
public interface FlowInterfaceConfigService extends BaseService<FlowInterfaceConfigDO> {

    /**
     * 对外接口调度
     */
     void executeFlowInterface();
}
