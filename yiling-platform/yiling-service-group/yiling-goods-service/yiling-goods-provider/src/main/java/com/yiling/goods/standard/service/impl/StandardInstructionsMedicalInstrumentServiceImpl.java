package com.yiling.goods.standard.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardInstructionsMedicalInstrumentMapper;
import com.yiling.goods.standard.entity.StandardInstructionsMedicalInstrumentDO;
import com.yiling.goods.standard.service.StandardInstructionsMedicalInstrumentService;

/**
 * @author shichen
 * @类名 StandardInstructionsMedicalInstrumentServiceImpl
 * @描述
 * @创建时间 2022/7/18
 * @修改人 shichen
 * @修改时间 2022/7/18
 **/
@Service
public class StandardInstructionsMedicalInstrumentServiceImpl extends BaseServiceImpl<StandardInstructionsMedicalInstrumentMapper, StandardInstructionsMedicalInstrumentDO> implements StandardInstructionsMedicalInstrumentService {
    @Override
    public StandardInstructionsMedicalInstrumentDO getInstructionsMedicalInstrumentByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsMedicalInstrumentDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsMedicalInstrumentDO :: getStandardId,standardId);
        return getOne(wrapper);
    }

    @Override
    public Boolean saveInstructionsMedicalInstrumentOne(StandardInstructionsMedicalInstrumentDO one) {
        return saveOrUpdate(one);
    }
}
