package com.yiling.open.erp.service.impl;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.open.erp.dao.SysHeartBeatMapper;
import com.yiling.open.erp.entity.SysHeartBeatDO;
import com.yiling.open.erp.service.ErpHeartService;

/**
 * @author: shuang.zhang
 * @date: 2021/7/20
 */
@Service
public class ErpHeartServiceImpl extends BaseServiceImpl<SysHeartBeatMapper, SysHeartBeatDO> implements ErpHeartService {

    @Override
    public Integer insertErpHeart(SysHeartBeatDO sysHeartBeat) {
        return this.baseMapper.insertSelective(sysHeartBeat);
    }
}
