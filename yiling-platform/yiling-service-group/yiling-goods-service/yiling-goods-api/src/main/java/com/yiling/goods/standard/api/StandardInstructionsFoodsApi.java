package com.yiling.goods.standard.api;

import com.yiling.goods.standard.dto.StandardInstructionsFoodsDTO;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
public interface StandardInstructionsFoodsApi {

    /**
     *  根据StandardId找到食品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsFoodsDTO getInstructionsFoodsByStandardId(Long standardId);
}
