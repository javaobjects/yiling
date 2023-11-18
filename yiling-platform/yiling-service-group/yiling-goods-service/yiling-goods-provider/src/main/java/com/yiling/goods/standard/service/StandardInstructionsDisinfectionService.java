package com.yiling.goods.standard.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardInstructionsDisinfectionDO;

/**
 * <p>
 * 消杀商品说明书 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardInstructionsDisinfectionService extends BaseService<StandardInstructionsDisinfectionDO> {

    /**
     * 根据StandardId找到消杀商品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsDisinfectionDO getInstructionsDisinfectionByStandardId(Long standardId);

    /**
     * 保存消杀商品说明书
     *
     * @param one
     * @return
     */
    Boolean saveInstructionsDisinfectionOne(StandardInstructionsDisinfectionDO one);
}