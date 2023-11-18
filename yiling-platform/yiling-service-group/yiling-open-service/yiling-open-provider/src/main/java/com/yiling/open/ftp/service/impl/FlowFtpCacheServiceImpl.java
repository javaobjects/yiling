package com.yiling.open.ftp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.ftp.dao.FlowFtpCacheMapper;
import com.yiling.open.ftp.entity.FlowFtpCacheDO;
import com.yiling.open.ftp.service.FlowFtpCacheService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-02-25
 */
@Service
public class FlowFtpCacheServiceImpl extends BaseServiceImpl<FlowFtpCacheMapper, FlowFtpCacheDO> implements FlowFtpCacheService {

    @Override
    public void addCache(List<BaseErpEntity> list, Long suId, String taskNo) {
        List<FlowFtpCacheDO> flowFtpCacheDOList = new ArrayList<>();
        for (BaseErpEntity baseErpEntity : list) {
            FlowFtpCacheDO flowFtpCacheDO = new FlowFtpCacheDO();
            flowFtpCacheDO.setSuId(suId);
            flowFtpCacheDO.setErpKey(baseErpEntity.getFlowKey());
            flowFtpCacheDO.setMd5(baseErpEntity.getFlowCacheFileMd5());
            flowFtpCacheDO.setType(taskNo);
            flowFtpCacheDOList.add(flowFtpCacheDO);
        }
        this.saveBatch(flowFtpCacheDOList);
    }

    @Override
    public void updateCache(List<BaseErpEntity> list, Long suId, String taskNo) {
        deleteCache(list, suId, taskNo);
        this.addCache(list, suId, taskNo);
    }

    @Override
    public void deleteCache(List<BaseErpEntity> list, Long suId, String taskNo) {
        List<String> keys=new ArrayList<>();
        for(BaseErpEntity baseErpEntity:list){
            keys.add(baseErpEntity.getFlowKey());
        }
        this.baseMapper.deleteCache(keys,suId,taskNo);
    }
}
