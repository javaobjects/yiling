package com.yiling.goods.standard.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardInstructionsDispensingGranuleMapper;
import com.yiling.goods.standard.entity.StandardInstructionsDecoctionDO;
import com.yiling.goods.standard.entity.StandardInstructionsDispensingGranuleDO;
import com.yiling.goods.standard.service.StandardInstructionsDispensingGranuleService;

/**
 * @author shichen
 * @类名 StandardInstructionsDispensingGranuleServiceImpl
 * @描述 配方颗粒
 * @创建时间 2023/5/17
 * @修改人 shichen
 * @修改时间 2023/5/17
 **/
@Service
public class StandardInstructionsDispensingGranuleServiceImpl extends BaseServiceImpl<StandardInstructionsDispensingGranuleMapper, StandardInstructionsDispensingGranuleDO> implements StandardInstructionsDispensingGranuleService {
    @Override
    public StandardInstructionsDispensingGranuleDO getInstructionsDispensingGranuleByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsDispensingGranuleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsDispensingGranuleDO :: getStandardId,standardId);
        return getOne(wrapper);
    }

    @Override
    public Boolean saveInstructionsDispensingGranuleOne(StandardInstructionsDispensingGranuleDO one) {
        return this.saveOrUpdate(one);
    }
}
