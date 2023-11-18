package com.yiling.goods.standard.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardInstructionsFoodsApi;
import com.yiling.goods.standard.dto.StandardInstructionsFoodsDTO;
import com.yiling.goods.standard.entity.StandardInstructionsFoodsDO;
import com.yiling.goods.standard.service.StandardInstructionsFoodsService;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
@DubboService
public class StandardInstructionsFoodsApiImpl implements StandardInstructionsFoodsApi {

    @Autowired
    StandardInstructionsFoodsService standardInstructionsFoodsService;

    /**
     * 根据StandardId找到食品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsFoodsDTO getInstructionsFoodsByStandardId(Long standardId) {
        StandardInstructionsFoodsDO foods = standardInstructionsFoodsService.getInstructionsFoodsByStandardId(standardId);
        return PojoUtils.map(foods,StandardInstructionsFoodsDTO.class);
    }
}
