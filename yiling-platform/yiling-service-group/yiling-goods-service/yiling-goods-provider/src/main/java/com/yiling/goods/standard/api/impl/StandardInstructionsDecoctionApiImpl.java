package com.yiling.goods.standard.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardInstructionsDecoctionApi;
import com.yiling.goods.standard.dto.StandardInstructionsDecoctionDTO;
import com.yiling.goods.standard.entity.StandardInstructionsDecoctionDO;
import com.yiling.goods.standard.service.StandardInstructionsDecoctionService;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
@DubboService
public class StandardInstructionsDecoctionApiImpl implements StandardInstructionsDecoctionApi {
    @Autowired
    StandardInstructionsDecoctionService standardInstructionsDecoctionService;
    /**
     * 根据StandardId找到中药饮片商品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsDecoctionDTO getInstructionsDecoctionByStandardId(Long standardId) {
        StandardInstructionsDecoctionDO decoction = standardInstructionsDecoctionService.getInstructionsDecoctionByStandardId(standardId);
        return PojoUtils.map(decoction,StandardInstructionsDecoctionDTO.class);
    }
}
