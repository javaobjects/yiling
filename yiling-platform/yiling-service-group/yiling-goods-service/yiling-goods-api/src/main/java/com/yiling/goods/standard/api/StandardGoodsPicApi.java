package com.yiling.goods.standard.api;


import java.util.List;
import java.util.Map;

import com.yiling.goods.standard.dto.StandardGoodsPicDTO;

/**
 * @author:wei.wang
 * @date:2021/5/21
 */
public interface StandardGoodsPicApi {
    /**
     * 删除图片
     * @param standardId 标准库商品
     * @param opUserId 操作人
     */
    void deleteStandardGoodsPicByStandardId(Long standardId,Long opUserId);

    /**
     * 通过standardId和标准商品规格ID查询规格图片 查询非规格图片 sellSpecificationsId为0
     *
     * @param standardId
     * @param sellSpecificationsId
     * @return
     */
    List<StandardGoodsPicDTO> getStandardGoodsPic(Long standardId , Long sellSpecificationsId);

    /**
     * 通过standardId查询规格图片
     *
     * @param standardIds
     * @return
     */
    Map<Long,List<StandardGoodsPicDTO>> getMapStandardGoodsPicByStandardId(List<Long> standardIds);
}
