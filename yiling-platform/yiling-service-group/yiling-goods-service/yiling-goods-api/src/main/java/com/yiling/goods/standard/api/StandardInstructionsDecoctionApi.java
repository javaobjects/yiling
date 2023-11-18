package com.yiling.goods.standard.api;

import com.yiling.goods.standard.dto.StandardInstructionsDecoctionDTO;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
public interface StandardInstructionsDecoctionApi {
    /**
     * 根据StandardId找到中药饮片商品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsDecoctionDTO getInstructionsDecoctionByStandardId(Long standardId);

}
