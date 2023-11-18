package com.yiling.open.erp.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.open.erp.entity.SysHeartBeatDO;

/**
 * @author: shuang.zhang
 * @date: 2021/7/20
 */
public interface ErpHeartService extends BaseService<SysHeartBeatDO> {

    Integer insertErpHeart(SysHeartBeatDO sysHeartBeat);
}
