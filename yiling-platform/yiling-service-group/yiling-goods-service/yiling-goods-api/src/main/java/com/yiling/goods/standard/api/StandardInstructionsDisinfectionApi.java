package com.yiling.goods.standard.api;

import com.yiling.goods.standard.dto.StandardInstructionsDisinfectionDTO;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
public interface StandardInstructionsDisinfectionApi {

    /**
     * 根据StandardId找到消杀商品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsDisinfectionDTO getInstructionsDisinfectionByStandardId(Long standardId);
}
