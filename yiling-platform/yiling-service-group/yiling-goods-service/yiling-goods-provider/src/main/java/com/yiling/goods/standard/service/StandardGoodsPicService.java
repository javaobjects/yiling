package com.yiling.goods.standard.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardGoodsPicDO;

/**
 * <p>
 * 标准库商品图片表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardGoodsPicService extends BaseService<StandardGoodsPicDO> {

    /**
     * 通过standardId和标准商品规格ID查询规格图片
     *
     * @param standardId
     * @param sellSpecificationsId
     * @return
     */
    List<StandardGoodsPicDO> getStandardGoodsPic(Long standardId , Long sellSpecificationsId);

    /**
     * 通过standardId查询规格图片
     *
     * @param standardId
     * @return
     */
    List<StandardGoodsPicDO> getStandardGoodsPicByStandardId(Long standardId);

    /**
     * 通过standardId查询规格图片
     *
     * @param standardIds
     * @return
     */
    Map<Long,List<StandardGoodsPicDO>> getMapStandardGoodsPicByStandardId(List<Long> standardIds);

    /**
     * 批量新增规格图片
     * @param list
     * @return
     */
    Boolean saveStandardGoodsPicBatch(List<StandardGoodsPicDO> list);

    /**
     * 删除图片
     * @param standardId 标准库商品
     * @param opUserId 操作人
     */
    void deleteStandardGoodsPicByStandardId(Long standardId,Long opUserId);
}
