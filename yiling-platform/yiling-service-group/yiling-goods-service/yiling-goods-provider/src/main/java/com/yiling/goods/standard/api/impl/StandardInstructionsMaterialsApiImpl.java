package com.yiling.goods.standard.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardInstructionsMaterialsApi;
import com.yiling.goods.standard.dto.StandardInstructionsMaterialsDTO;
import com.yiling.goods.standard.entity.StandardInstructionsMaterialsDO;
import com.yiling.goods.standard.service.StandardInstructionsMaterialsService;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
@DubboService
public class StandardInstructionsMaterialsApiImpl implements StandardInstructionsMaterialsApi {

    @Autowired
    StandardInstructionsMaterialsService standardInstructionsMaterialsService;

    /**
     * 根据StandardId找到中药材商品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsMaterialsDTO getInstructionsMaterialsByStandardId(Long standardId) {
        StandardInstructionsMaterialsDO materials = standardInstructionsMaterialsService.getInstructionsMaterialsByStandardId(standardId);
        return PojoUtils.map(materials,StandardInstructionsMaterialsDTO.class);
    }
}
