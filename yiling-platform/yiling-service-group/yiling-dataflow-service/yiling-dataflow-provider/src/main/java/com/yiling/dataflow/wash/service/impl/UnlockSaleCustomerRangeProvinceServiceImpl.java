package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.wash.dao.UnlockSaleCustomerRangeProvinceMapper;
import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleCustomerRangeProvinceRequest;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerRangeDO;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerRangeProvinceDO;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerRangeProvinceService;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerRangeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-28
 */
@Service
public class UnlockSaleCustomerRangeProvinceServiceImpl extends BaseServiceImpl<UnlockSaleCustomerRangeProvinceMapper, UnlockSaleCustomerRangeProvinceDO> implements UnlockSaleCustomerRangeProvinceService {


    @Autowired
    private UnlockSaleCustomerRangeService unlockSaleCustomerRangeService;

    @Override
    public List<String> getProvinceListByUscId(Long uscId) {
        LambdaQueryWrapper<UnlockSaleCustomerRangeProvinceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleCustomerRangeProvinceDO::getUscId, uscId);
        List<UnlockSaleCustomerRangeProvinceDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.empty();
        }
        return list.stream().map(e -> e.getProvinceCode()).collect(Collectors.toList());
    }

    @Override
    public boolean save(SaveUnlockSaleCustomerRangeProvinceRequest request) {
        Long uscId = request.getUscId();
        if (uscId == null || uscId == 0) {
            UnlockSaleCustomerRangeDO unlockSaleCustomerRangeDO = new UnlockSaleCustomerRangeDO();
            unlockSaleCustomerRangeDO.setRuleId(request.getRuleId());
            unlockSaleCustomerRangeDO.setOpUserId(request.getOpUserId());
            unlockSaleCustomerRangeService.save(unlockSaleCustomerRangeDO);
            uscId = unlockSaleCustomerRangeDO.getId();
        }

        List<String> provinceCodeList = getProvinceListByUscId(uscId);

        List<UnlockSaleCustomerRangeProvinceDO> list = new ArrayList<>();
        for (String provinceCode : request.getProvinceCodeList()) {
            //新增的区域
            if (!provinceCodeList.contains(provinceCode)) {
                UnlockSaleCustomerRangeProvinceDO unlockSaleCustomerRangeProvinceDO = new UnlockSaleCustomerRangeProvinceDO();
                unlockSaleCustomerRangeProvinceDO.setUscId(uscId);
                unlockSaleCustomerRangeProvinceDO.setProvinceCode(provinceCode);
                unlockSaleCustomerRangeProvinceDO.setOpUserId(request.getOpUserId());
                list.add(unlockSaleCustomerRangeProvinceDO);
            }
        }
        //删除数据 原有数据移除现有传进来的数据
        provinceCodeList.removeAll(request.getProvinceCodeList());

        if (CollUtil.isNotEmpty(provinceCodeList)) {
            LambdaQueryWrapper<UnlockSaleCustomerRangeProvinceDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UnlockSaleCustomerRangeProvinceDO::getUscId, uscId);
            wrapper.in(UnlockSaleCustomerRangeProvinceDO::getProvinceCode, provinceCodeList);

            UnlockSaleCustomerRangeProvinceDO unlockSaleCustomerRangeProvinceDO = new UnlockSaleCustomerRangeProvinceDO();
            unlockSaleCustomerRangeProvinceDO.setOpUserId(request.getOpUserId());
            this.batchDeleteWithFill(unlockSaleCustomerRangeProvinceDO, wrapper);
        }

        if (CollUtil.isNotEmpty(list)) {
            this.saveOrUpdateBatch(list);
        }
        return true;
    }
}
