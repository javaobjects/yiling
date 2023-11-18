package com.yiling.goods.standard.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardInstructionsGoodsApi;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.goods.standard.entity.StandardInstructionsGoodsDO;
import com.yiling.goods.standard.service.StandardInstructionsGoodsService;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
@DubboService
public class StandardInstructionsGoodsApiImpl implements StandardInstructionsGoodsApi {

    @Autowired
    StandardInstructionsGoodsService standardInstructionsGoodsService;

    /**
     * 根据StandardId找到药品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsGoodsDTO getInstructionsGoodsByStandardId(Long standardId) {
        StandardInstructionsGoodsDO goods = standardInstructionsGoodsService.getInstructionsGoodsByStandardId(standardId);
        StandardInstructionsGoodsDTO dto = PojoUtils.map(goods, StandardInstructionsGoodsDTO.class);
        return dto;
    }

    /**
     * 根据StandardIds找到药品说明书
     *
     * @param standardIds
     * @return
     */
    @Override
    public List<StandardInstructionsGoodsDTO> getInstructionsGoodsByStandardIdList(List<Long> standardIds) {
        return standardInstructionsGoodsService.getInstructionsGoodsByStandardIdList(standardIds);
    }
}
