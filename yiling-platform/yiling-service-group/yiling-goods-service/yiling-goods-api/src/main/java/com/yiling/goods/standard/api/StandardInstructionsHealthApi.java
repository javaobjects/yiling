package com.yiling.goods.standard.api;

import java.util.List;

import com.yiling.goods.standard.dto.StandardInstructionsHealthDTO;

/**
 * @author:wei.wang
 * @date:2021/6/16
 */
public interface StandardInstructionsHealthApi {

    /**
     * 根据StandardId找到保健食品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsHealthDTO getInstructionsHealthByStandardId(Long standardId);

    /**
     * 根据StandardIds找到保健食品说明书
     * @param standardIds
     * @return
     */
    List<StandardInstructionsHealthDTO> getInstructionsHealthByStandardIdList(List<Long> standardIds );
}
