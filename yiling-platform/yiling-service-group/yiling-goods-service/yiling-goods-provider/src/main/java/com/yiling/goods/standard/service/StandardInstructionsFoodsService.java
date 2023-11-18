package com.yiling.goods.standard.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardInstructionsFoodsDO;

/**
 * <p>
 * 食品说明书 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardInstructionsFoodsService extends BaseService<StandardInstructionsFoodsDO> {

    /**
     *  根据StandardId找到食品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsFoodsDO getInstructionsFoodsByStandardId(Long standardId);

    /**
     * 保存食品说明书
     *
     * @param one
     * @return
     */
    Boolean saveInstructionsFoodsOne(StandardInstructionsFoodsDO one);

}
