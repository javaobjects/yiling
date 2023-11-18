package com.yiling.goods.standard.api;

import java.util.List;

import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
public interface StandardInstructionsGoodsApi {
    /**
     * 根据StandardId找到药品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsGoodsDTO getInstructionsGoodsByStandardId(Long standardId);

    /**
     * 根据StandardIds找到药品说明书
     * @param standardIds
     * @return
     */
    List<StandardInstructionsGoodsDTO> getInstructionsGoodsByStandardIdList(List<Long> standardIds);
}
