package com.yiling.dataflow.flow.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.dataflow.flow.dao.FlowCrmSaleMapper;
import com.yiling.dataflow.flow.entity.FlowCrmSaleDO;
import com.yiling.dataflow.flow.service.FlowCrmSaleService;
import com.yiling.framework.common.base.BaseServiceImpl;

/**
 * @author fucheng.bai
 * @date 2023/2/2
 */
@Service
public class FlowCrmSaleServiceImpl extends BaseServiceImpl<FlowCrmSaleMapper, FlowCrmSaleDO> implements FlowCrmSaleService {

    @Override
    public void batchInsert(List<FlowCrmSaleDO> flowCrmSaleDOList) {
        baseMapper.insertBatchSomeColumn(flowCrmSaleDOList);
    }

    @Override
    public void updateBySourceFile(String sourceFile, Integer status) {
        LambdaUpdateWrapper<FlowCrmSaleDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(FlowCrmSaleDO::getSourceFile, sourceFile);
        wrapper.eq(FlowCrmSaleDO::getStatus, 1);

        FlowCrmSaleDO flowCrmSaleDO = new FlowCrmSaleDO();
        flowCrmSaleDO.setStatus(2);
        flowCrmSaleDO.setUpdateTime(new Date());
        baseMapper.update(flowCrmSaleDO, wrapper);
    }

    @Override
    public void deleteBySourceFile(String sourceFile, Integer status) {
        LambdaUpdateWrapper<FlowCrmSaleDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(FlowCrmSaleDO::getSourceFile, sourceFile);
        wrapper.eq(FlowCrmSaleDO::getStatus, 1);
        baseMapper.delete(wrapper);
    }

    @Override
    public List<FlowCrmSaleDO> getList(String ename, String enterpriseName, Date soTime, String crmGoodsCode, BigDecimal soQuantity, String soBatchNo) {
        LambdaQueryWrapper<FlowCrmSaleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowCrmSaleDO::getEname, ename);
        wrapper.eq(FlowCrmSaleDO::getEnterpriseName, enterpriseName);
        wrapper.eq(FlowCrmSaleDO::getSoTime, soTime);
        wrapper.eq(FlowCrmSaleDO::getCrmGoodsCode, crmGoodsCode);
        wrapper.eq(FlowCrmSaleDO::getSoQuantity, soQuantity);
        wrapper.eq(FlowCrmSaleDO::getSoBatchNo, soBatchNo);

        return baseMapper.selectList(wrapper);
    }
}
