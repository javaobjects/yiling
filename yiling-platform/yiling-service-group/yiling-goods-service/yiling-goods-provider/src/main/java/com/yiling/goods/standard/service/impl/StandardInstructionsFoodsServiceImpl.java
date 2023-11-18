package com.yiling.goods.standard.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardInstructionsFoodsMapper;
import com.yiling.goods.standard.entity.StandardInstructionsFoodsDO;
import com.yiling.goods.standard.service.StandardInstructionsFoodsService;

/**
 * <p>
 * 食品说明书 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardInstructionsFoodsServiceImpl extends BaseServiceImpl<StandardInstructionsFoodsMapper, StandardInstructionsFoodsDO> implements StandardInstructionsFoodsService {

    /**
     * 根据StandardId找到食品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsFoodsDO getInstructionsFoodsByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsFoodsDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsFoodsDO :: getStandardId,standardId);
        return getOne(wrapper);
    }

    /**
     * 保存食品说明书
     *
     * @param one
     * @return
     */
    @Override
    public Boolean saveInstructionsFoodsOne(StandardInstructionsFoodsDO one) {
        return saveOrUpdate(one);
    }
}
