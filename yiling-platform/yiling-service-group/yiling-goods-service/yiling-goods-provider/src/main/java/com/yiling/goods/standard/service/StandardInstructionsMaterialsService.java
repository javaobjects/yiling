package com.yiling.goods.standard.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.entity.StandardInstructionsMaterialsDO;

/**
 * <p>
 * 中药材商品说明书 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardInstructionsMaterialsService extends BaseService<StandardInstructionsMaterialsDO> {

    /**
     * 根据StandardId找到中药材商品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsMaterialsDO getInstructionsMaterialsByStandardId(Long standardId);

    /**
     * 中药材商品说明书
     * @param one
     * @return
     */
    Boolean saveInstructionsMaterialsOne(StandardInstructionsMaterialsDO one );
}
