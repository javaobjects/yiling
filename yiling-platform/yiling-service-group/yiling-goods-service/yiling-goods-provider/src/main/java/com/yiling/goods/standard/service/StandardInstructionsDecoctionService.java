package com.yiling.goods.standard.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardInstructionsDecoctionDO;

/**
 * <p>
 * 中药饮片商品说明书 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardInstructionsDecoctionService extends BaseService<StandardInstructionsDecoctionDO> {

    /**
     * 根据StandardId找到中药饮片商品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsDecoctionDO getInstructionsDecoctionByStandardId(Long standardId);

    /**
     * 保存中药饮片商品说明书
     *
     * @param one
     * @return
     */
    Boolean saveInstructionsDecoctionOne(StandardInstructionsDecoctionDO one);
}
