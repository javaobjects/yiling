package com.yiling.goods.standard.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsPicApi;
import com.yiling.goods.standard.dto.StandardGoodsPicDTO;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;
import com.yiling.goods.standard.service.StandardGoodsPicService;


/**
 * @author:wei.wang
 * @date:2021/5/25
 */
@DubboService
public class StandardGoodsPicApiImpl implements StandardGoodsPicApi {

    @Autowired
    private StandardGoodsPicService standardGoodsPicService;
    /**
     * 删除图片
     * @param standardId 标准库商品
     * @param opUserId 操作人
     */
    @Override
    public void deleteStandardGoodsPicByStandardId(Long standardId,Long opUserId) {
        standardGoodsPicService.deleteStandardGoodsPicByStandardId(standardId,opUserId);
    }

    /**
     * 通过standardId和标准商品规格ID查询规格图片
     *
     * @param standardId
     * @param sellSpecificationsId
     * @return
     */
    @Override
        public List<StandardGoodsPicDTO> getStandardGoodsPic(Long standardId, Long sellSpecificationsId) {
        List<StandardGoodsPicDO> goodsPic = standardGoodsPicService.getStandardGoodsPic(standardId, sellSpecificationsId);
        return PojoUtils.map(goodsPic,StandardGoodsPicDTO.class);
    }

    /**
     * 通过standardId查询规格图片
     *
     * @param standardIds
     * @return
     */
    @Override
    public Map<Long, List<StandardGoodsPicDTO>> getMapStandardGoodsPicByStandardId(List<Long> standardIds) {
        Map<Long, List<StandardGoodsPicDO>> map = standardGoodsPicService.getMapStandardGoodsPicByStandardId(standardIds);
        Map<Long, List<StandardGoodsPicDTO>> result = PojoUtils.map(map, StandardGoodsPicDTO.class);
        return result;
    }
}
