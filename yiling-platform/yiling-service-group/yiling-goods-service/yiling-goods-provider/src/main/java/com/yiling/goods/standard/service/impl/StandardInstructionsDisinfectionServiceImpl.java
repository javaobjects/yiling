package com.yiling.goods.standard.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.standard.dao.StandardInstructionsDisinfectionMapper;
import com.yiling.goods.standard.entity.StandardInstructionsDisinfectionDO;
import com.yiling.goods.standard.service.StandardInstructionsDisinfectionService;

/**
 * <p>
 * 消杀商品说明书 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Service
public class StandardInstructionsDisinfectionServiceImpl extends BaseServiceImpl<StandardInstructionsDisinfectionMapper, StandardInstructionsDisinfectionDO> implements StandardInstructionsDisinfectionService {

    /**
     * 根据StandardId找到消杀商品说明书
     *
     * @param standardId
     * @return
     */
    @Override
    public StandardInstructionsDisinfectionDO getInstructionsDisinfectionByStandardId(Long standardId) {
        QueryWrapper<StandardInstructionsDisinfectionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StandardInstructionsDisinfectionDO :: getStandardId,standardId);
        return getOne(wrapper);
    }

    /**
     * 保存消杀商品说明书
     *
     * @param one
     * @return
     */
    @Override
    public Boolean saveInstructionsDisinfectionOne(StandardInstructionsDisinfectionDO one) {
        return saveOrUpdate(one);
    }

}
