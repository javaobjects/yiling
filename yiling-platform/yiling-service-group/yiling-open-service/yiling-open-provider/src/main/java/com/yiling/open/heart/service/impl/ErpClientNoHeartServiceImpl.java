package com.yiling.open.heart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.open.heart.dao.ErpClientNoHeartMapper;
import com.yiling.open.heart.dto.request.SaveErpClientNoHeartRequest;
import com.yiling.open.heart.entity.ErpClientNoHeartDO;
import com.yiling.open.heart.service.ErpClientNoHeartService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Assert;

/**
 * @author: houjie.sun
 * @date: 2022/10/27
 */
@Service
public class ErpClientNoHeartServiceImpl extends BaseServiceImpl<ErpClientNoHeartMapper, ErpClientNoHeartDO> implements ErpClientNoHeartService {

    @Override
    public Boolean saveBatchErpClientNoHeart(List<SaveErpClientNoHeartRequest> requestList, DateTime taskTime) {
        Assert.notEmpty(requestList, "保存参数不能为空");
        requestList.forEach(o -> {
            Assert.notNull(o, "保存参数不能为空");
        });

        List<ErpClientNoHeartDO> saveEntityList = new ArrayList<>();
        for (SaveErpClientNoHeartRequest saveRequest : requestList) {
            ErpClientNoHeartDO entity = new ErpClientNoHeartDO();
            entity.setSuId(saveRequest.getSuId());
            entity.setSuDeptNo(saveRequest.getSuDeptNo());
            entity.setRkSuId(saveRequest.getRkSuId());
            entity.setClientName(saveRequest.getClientName());
            entity.setFlowMode(saveRequest.getFlowMode());
            entity.setInstallEmployee(saveRequest.getInstallEmployee());
            entity.setCrmDepartment(saveRequest.getCrmDepartment());
            entity.setCrmProvince(saveRequest.getCrmProvince());
            entity.setCrmCommerceJobNumber(saveRequest.getCrmCommerceJobNumber());
            entity.setCrmCommerceLiablePerson(saveRequest.getCrmCommerceLiablePerson());
            entity.setTaskTime(saveRequest.getTaskTime());
            entity.setOpTime(saveRequest.getOpTime());
            entity.setOpUserId(saveRequest.getOpUserId());
            saveEntityList.add(entity);
        }

        if(CollUtil.isNotEmpty(saveEntityList)){
            this.saveBatch(saveEntityList, 1000);
        }
        return true;
    }

    @Override
    public Integer deleteByTaskTime(DateTime taskTime) {
        return this.baseMapper.deleteByTaskTime(taskTime);
    }

}
