package com.yiling.open.erp.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpDeleteDataApi;
import com.yiling.open.erp.dto.ErpDeleteDataDTO;
import com.yiling.open.erp.dto.request.SaveErpDeleteDataRequest;
import com.yiling.open.erp.entity.ErpDeleteDataDO;
import com.yiling.open.erp.service.ErpDeleteDataService;

/**
 * @author: shuang.zhang
 * @date: 2021/8/13
 */

@DubboService
public class ErpDeleteDataApiImpl implements ErpDeleteDataApi {

    @Autowired
    private ErpDeleteDataService erpDeleteDataService;

    @Override
    public boolean saveOrUpdateErpDeleteData(SaveErpDeleteDataRequest request) {
        ErpDeleteDataDO erpDeleteDataDO=PojoUtils.map(request, ErpDeleteDataDO.class);
        QueryWrapper<ErpDeleteDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpDeleteDataDO::getSuId, request.getSuId());
        queryWrapper.lambda().eq(ErpDeleteDataDO::getTaskNo, request.getTaskNo());
        queryWrapper.lambda().eq(ErpDeleteDataDO::getDataId, request.getDataId());
        queryWrapper.lambda().eq(ErpDeleteDataDO::getStatus, 0);
        ErpDeleteDataDO deleteDataDO=erpDeleteDataService.getOne(queryWrapper);
        if(deleteDataDO==null) {
            return erpDeleteDataService.saveOrUpdate(erpDeleteDataDO);
        }
        return true;
    }

    @Override
    public boolean updateErpDeleteData(SaveErpDeleteDataRequest request) {
        QueryWrapper<ErpDeleteDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpDeleteDataDO::getSuId, request.getSuId());
        queryWrapper.lambda().eq(ErpDeleteDataDO::getTaskNo, request.getTaskNo());
        queryWrapper.lambda().eq(ErpDeleteDataDO::getDataId, request.getDataId());
        ErpDeleteDataDO erpDeleteDataDO=new ErpDeleteDataDO();
        erpDeleteDataDO.setStatus(1);
        erpDeleteDataDO.setUpdateTime(new Date());
        return erpDeleteDataService.update(erpDeleteDataDO,queryWrapper);
    }

    @Override
    public List<ErpDeleteDataDTO> findErpDeleteBySuIdAndTaskNo(Long suId, String taskNo) {
        QueryWrapper<ErpDeleteDataDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ErpDeleteDataDO::getSuId, suId);
        queryWrapper.lambda().eq(ErpDeleteDataDO::getTaskNo, taskNo);
        queryWrapper.lambda().eq(ErpDeleteDataDO::getStatus,0);

        return PojoUtils.map(erpDeleteDataService.list(queryWrapper), ErpDeleteDataDTO.class);
    }
}
