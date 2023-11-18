package com.yiling.goods.standard.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardInstructionsHealthApi;
import com.yiling.goods.standard.dto.StandardInstructionsHealthDTO;
import com.yiling.goods.standard.entity.StandardInstructionsHealthDO;
import com.yiling.goods.standard.service.StandardInstructionsHealthService;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
@DubboService
public class StandardInstructionsHealthApiImpl implements StandardInstructionsHealthApi {

    @Autowired
    StandardInstructionsHealthService standardInstructionsHealthService;
    /**
     * 根据StandardId找到保健食品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsHealthDTO getInstructionsHealthByStandardId(Long standardId) {
        StandardInstructionsHealthDO health = standardInstructionsHealthService.getInstructionsHealthByStandardId(standardId);
        StandardInstructionsHealthDTO dto = PojoUtils.map(health, StandardInstructionsHealthDTO.class);
        return dto;
    }

    /**
     * 根据StandardIds找到保健食品说明书
     *
     * @param standardIds
     * @return
     */
    @Override
    public List<StandardInstructionsHealthDTO> getInstructionsHealthByStandardIdList(List<Long> standardIds) {
        return standardInstructionsHealthService.getInstructionsHealthByStandardIdList(standardIds);
    }
}
