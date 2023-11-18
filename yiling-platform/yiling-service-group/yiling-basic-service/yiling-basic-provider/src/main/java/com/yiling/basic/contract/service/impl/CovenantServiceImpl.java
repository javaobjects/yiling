package com.yiling.basic.contract.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.basic.contract.dao.CovenantMapper;
import com.yiling.basic.contract.entity.CovenantDO;
import com.yiling.basic.contract.service.CovenantService;
import com.yiling.framework.common.base.BaseServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/11/11
 */
@Slf4j
@Service
public class CovenantServiceImpl extends BaseServiceImpl<CovenantMapper, CovenantDO> implements CovenantService {

    @Override
    public CovenantDO getByQysContractId(Long qysContractId) {
        LambdaQueryWrapper<CovenantDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CovenantDO::getQysContractId, qysContractId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void updateStatus(Long id, String status) {
        CovenantDO covenantDO = new CovenantDO();
        covenantDO.setId(id);
        covenantDO.setStatus(status);
        covenantDO.setUpdateTime(new Date());
        baseMapper.updateById(covenantDO);
    }

    @Override
    public void updateFileKey(Long id, String fileKey) {
        CovenantDO covenantDO = new CovenantDO();
        covenantDO.setId(id);
        covenantDO.setFileKey(fileKey);
        covenantDO.setUpdateTime(new Date());
        baseMapper.updateById(covenantDO);
    }
}
