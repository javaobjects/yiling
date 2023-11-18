package com.yiling.goods.standard.api;

import com.yiling.goods.standard.dto.StandardInstructionsMaterialsDTO;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
public interface StandardInstructionsMaterialsApi {
    /**
     * 根据StandardId找到中药材商品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsMaterialsDTO getInstructionsMaterialsByStandardId(Long standardId);
}
