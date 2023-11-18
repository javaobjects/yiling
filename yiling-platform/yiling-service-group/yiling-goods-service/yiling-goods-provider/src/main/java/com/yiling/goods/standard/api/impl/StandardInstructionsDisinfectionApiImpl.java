package com.yiling.goods.standard.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardInstructionsDisinfectionApi;
import com.yiling.goods.standard.dto.StandardInstructionsDisinfectionDTO;
import com.yiling.goods.standard.entity.StandardInstructionsDisinfectionDO;
import com.yiling.goods.standard.service.StandardInstructionsDisinfectionService;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
@DubboService
public class StandardInstructionsDisinfectionApiImpl implements StandardInstructionsDisinfectionApi {

    @Autowired
    StandardInstructionsDisinfectionService standardInstructionsDisinfectionService;
    /**
     * 根据StandardId找到消杀商品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsDisinfectionDTO getInstructionsDisinfectionByStandardId(Long standardId) {
        StandardInstructionsDisinfectionDO disinfection = standardInstructionsDisinfectionService.getInstructionsDisinfectionByStandardId(standardId);
        return PojoUtils.map(disinfection,StandardInstructionsDisinfectionDTO.class);
    }
}
