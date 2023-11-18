package com.yiling.goods.standard.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.standard.dto.StandardInstructionsGoodsDTO;
import com.yiling.goods.standard.entity.StandardInstructionsGoodsDO;

/**
 * <p>
 * 标准库商品说明书 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
public interface StandardInstructionsGoodsService extends BaseService<StandardInstructionsGoodsDO> {

    /**
     * 根据StandardId找到药品说明书
     *
     * @param standardId
     * @return
     */
    StandardInstructionsGoodsDO getInstructionsGoodsByStandardId(Long standardId);

    /**
     * 根据StandardIds找到药品说明书
     * @param standardIds
     * @return
     */
    List<StandardInstructionsGoodsDTO> getInstructionsGoodsByStandardIdList(List<Long> standardIds);

    /**
     * 保存普通药品说明书
     * @param one
     * @return
     */
    Boolean saveInstructionsGoodsOne(StandardInstructionsGoodsDO one );
 }
